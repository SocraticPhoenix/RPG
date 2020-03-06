package com.gmail.socraticphoenix.rpg.conversation;

import com.flowpowered.math.vector.Vector3d;
import com.gmail.socraticphoenix.rpg.translation.Messages;
import org.spongepowered.api.effect.sound.SoundType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiPredicate;

public class ConversationNode {
    private String key;
    private String promptKey;
    private List<ConversationNode> options;
    private BiPredicate<ConversationNode, Player> selected;

    public ConversationNode(String key, String promptKey, List<ConversationNode> options, BiPredicate<ConversationNode, Player> selected) {
        this.key = key;
        this.promptKey = promptKey;
        this.options = options;
        this.selected = selected;
    }

    public void sendTo(Text prefix, Player player, Vector3d location) {
        sendTo(prefix, player, location, new AtomicBoolean(false));
    }

    public void sendTo(Text prefix, Player player, Vector3d location, AtomicBoolean selected) {
        long current = System.currentTimeMillis();
        long cd = current + TimeUnit.MINUTES.toMillis(1);

        Text message = Text.of(prefix, TextColors.GREEN, Messages.translate(player, this.promptKey));
        player.sendMessage(message);

        this.options.forEach(node -> {
            Text txt = Text.of(TextColors.GOLD, " - ", Messages.translate(player, node.getKey()));
            txt = Text.builder().append(txt).onClick(TextActions.executeCallback(src -> {
                if (!selected.get()) {
                    if (System.currentTimeMillis() <= cd) {
                        if (location.distanceSquared(player.getPosition()) < 5 * 5) {
                            selected.set(node.getSelected().test(this, player));
                            if (node.getPromptKey() != null) {
                                player.stopSounds(SoundType.builder().build(this.promptKey));
                                player.playSound(SoundType.builder().build(node.getPromptKey()), location, 1.0);
                                node.sendTo(prefix, player, location, selected);
                            } else {
                                selected.set(true);
                            }
                        } else {
                            player.sendMessage(Text.of(TextColors.RED, Messages.translate(player, "rpg.conversation.distance")));
                        }
                    } else {
                        player.sendMessage(Text.of(TextColors.RED, Messages.translate(player, "rpg.conversation.expire")));
                    }
                } else {
                    player.sendMessage(Text.of(TextColors.RED, Messages.translate(player, "rpg.conversation.selected")));
                }
            })).build();
            player.sendMessage(txt);
        });
    }

    public BiPredicate<ConversationNode, Player> getSelected() {
        return selected;
    }

    public String getPromptKey() {
        return promptKey;
    }

    public String getKey() {
        return key;
    }

    public List<ConversationNode> getOptions() {
        return options;
    }

    public static ConversationNode.Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String key;
        private String promptKey;
        private List<ConversationNode> options;
        private BiPredicate<ConversationNode, Player> selected;

        public Builder() {
            this.options = new ArrayList<>();
            this.selected = (c, p) -> false;
        }

        public Builder key(String key) {
            this.key = key;
            return this;
        }

        public Builder promptKey(String key) {
            this.promptKey = key;
            return this;
        }

        public Builder option(ConversationNode node) {
            this.options.add(node);
            return this;
        }

        public Builder options(ConversationNode... nodes) {
            Collections.addAll(this.options, nodes);
            return this;
        }

        public Builder selected(BiPredicate<ConversationNode, Player> selected) {
            this.selected = selected;
            return this;
        }

        public ConversationNode build() {
            return new ConversationNode(this.key, this.promptKey, this.options, this.selected);
        }

    }

}
