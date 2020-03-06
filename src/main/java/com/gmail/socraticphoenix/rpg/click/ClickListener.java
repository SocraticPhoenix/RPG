package com.gmail.socraticphoenix.rpg.click;

import com.flowpowered.math.imaginary.Quaterniond;
import com.flowpowered.math.vector.Vector3d;
import com.gmail.socraticphoenix.collect.Items;
import com.gmail.socraticphoenix.parse.Strings;
import com.gmail.socraticphoenix.rpg.click.data.ImmutableItemClickData;
import com.gmail.socraticphoenix.rpg.click.data.PlayerClickData;
import com.gmail.socraticphoenix.rpg.event.RPGClickEvent;
import com.gmail.socraticphoenix.rpg.translation.Messages;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.entity.InteractEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.filter.type.Include;
import org.spongepowered.api.event.item.inventory.ChangeInventoryEvent;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.title.Title;

import java.util.concurrent.TimeUnit;

public class ClickListener {

    @Listener
    @Include({ChangeInventoryEvent.SwapHand.class, ChangeInventoryEvent.Held.class})
    public void onSlotChange(ChangeInventoryEvent ev, @First Player player) {
        player.get(PlayerClickData.class).ifPresent(data -> player.offer(data.sequence().removeAll(t -> true)));
        player.sendTitle(Title.builder()
                .actionBar(Text.of(""))
                .stay(1)
                .build());
    }

    @Listener
    @Include({InteractItemEvent.Primary.class, InteractItemEvent.Secondary.class})
    public void onClickItem(InteractItemEvent event, @First Player player) {
        ClickType type;
        if (event instanceof InteractItemEvent.Primary) {
            type = ClickType.PRIMARY;
        } else {
            type = ClickType.SECONDARY;
        }

        handle(player, event.getItemStack(), type, event);
    }

    @Listener
    public void onBlock(InteractBlockEvent.Primary ev, @First Player player) {
        handle(player, player.getItemInHand(HandTypes.MAIN_HAND).orElse(ItemStack.empty()).createSnapshot(), ClickType.PRIMARY, ev);
    }

    @Listener
    public void onEntity(InteractEntityEvent.Primary ev, @First Player player) {
        handle(player, player.getItemInHand(HandTypes.MAIN_HAND).orElse(ItemStack.empty()).createSnapshot(), ClickType.PRIMARY, ev);
    }

    public static void handle(Player player, ItemStackSnapshot itemStack, ClickType type, Cancellable event) {
        itemStack.get(ImmutableItemClickData.class).ifPresent(itemData -> {
            event.setCancelled(true);

            PlayerClickData data = player.getOrCreate(PlayerClickData.class).get();
            player.offer(data);
            long time = System.currentTimeMillis();

            if (time - data.lastClicked().get() > itemData.clickInterval().get()) {
                player.offer(data.sequence().removeAll(t -> true));
                data = player.getOrCreate(PlayerClickData.class).get();
            }

            player.offer(data.sequence().add(type));
            data = player.getOrCreate(PlayerClickData.class).get();

            if (itemData.predicate().get().getPredicate().test(itemStack.createStack(), data.sequence().get())) {
                RPGClickEvent rpgClickEvent = new RPGClickEvent(itemStack.createStack(), Items.looseClone(data.sequence().get()), player);
                Sponge.getEventManager().post(rpgClickEvent);
                player.clearTitle();

                player.offer(data.sequence().removeAll(t -> true));
                data = player.getOrCreate(PlayerClickData.class).get();
            } else {
                displayClicks(player, itemData.clickInterval().get());
            }

            player.offer(data.lastClicked().set(time));
        });
    }

    private static void displayClicks(Player player, long interval) {
        player.get(PlayerClickData.class).ifPresent(data -> player.sendTitle(Title.builder()
                .actionBar(Text.builder(Strings.glue(" - ", data.sequence().get().stream().map(d -> Messages.translateString(player, d.getName())).toArray())).color(TextColors.WHITE).build())
                .stay((int) (TimeUnit.MILLISECONDS.toSeconds(interval) * 20 * 0.7))
                .fadeOut((int) (TimeUnit.MILLISECONDS.toSeconds(interval) * 20 * 0.2))
                .build()));
    }

}
