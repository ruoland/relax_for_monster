package com.example.examplemod.dictionary;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.TextComponentTagVisitor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.server.commands.SayCommand;
import net.minecraft.world.entity.player.Player;

public class DictionaryCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("dic").executes((command) ->{
            return execute(command);
        }));
    }

    public static int execute(CommandContext<CommandSourceStack> context){
        if(context.getSource().isPlayer()){
            Player player = context.getSource().getPlayer();

            player.sendSystemMessage(Component.literal("안녀여여엉"));
        }
        return Command.SINGLE_SUCCESS;
    }
}