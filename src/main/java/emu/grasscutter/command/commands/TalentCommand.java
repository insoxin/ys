package emu.grasscutter.command.commands;

import emu.grasscutter.command.Command;
import emu.grasscutter.command.CommandHandler;
import emu.grasscutter.game.GenshinPlayer;
import emu.grasscutter.game.avatar.GenshinAvatar;
import emu.grasscutter.game.entity.EntityAvatar;
import emu.grasscutter.server.packet.send.PacketAvatarSkillChangeNotify;
import emu.grasscutter.server.packet.send.PacketAvatarSkillUpgradeRsp;

import java.util.List;

@Command(label = "talent", usage = "talent <talentID> <value>",
        description = "Set talent level for your current active character", permission = "player.settalent")
public class TalentCommand implements CommandHandler {

    @Override
    public void execute(GenshinPlayer sender, List<String> args) {
        if (sender == null) {
            CommandHandler.sendMessage(null, "Run this command in-game.");
            return;
        }

        if (args.size() < 0 || args.size() < 1){
            CommandHandler.sendMessage(sender, "To set talent level: /talent set <talentID> <value>");
            CommandHandler.sendMessage(sender, "To get talent ID: /talent getid");
            return;
        }

        String cmdSwitch = args.get(0);
        switch (cmdSwitch) {
            default:
                CommandHandler.sendMessage(sender, "To set talent level: /talent set <talentID> <value>");
                CommandHandler.sendMessage(sender, "To get talent ID: /talent getid");
            return;
            case "set":
                    try {
                        int skillId = Integer.parseInt(args.get(1));
                        int nextLevel = Integer.parseInt(args.get(2));
                        EntityAvatar entity = sender.getTeamManager().getCurrentAvatarEntity();
                        GenshinAvatar avatar = entity.getAvatar(); 
                        int skillIdNorAtk = avatar.getData().getSkillDepot().getSkills().get(0);
                        int skillIdE = avatar.getData().getSkillDepot().getSkills().get(1);
                        int skillIdQ = avatar.getData().getSkillDepot().getEnergySkill();
                        int currentLevelNorAtk = avatar.getSkillLevelMap().get(skillIdNorAtk);
                        int currentLevelE = avatar.getSkillLevelMap().get(skillIdE);
                        int currentLevelQ = avatar.getSkillLevelMap().get(skillIdQ);
                        if (args.size() < 2){
                            CommandHandler.sendMessage(sender, "To set talent level: /talent set <talentID> <value>");
                            CommandHandler.sendMessage(sender, "To get talent ID: /talent getid");
                            return;
                        }
                        if (nextLevel > 16){ 
                            CommandHandler.sendMessage(sender, "Invalid talent level. Level should be lower than 16");
                            return;
                        }
                            if (skillId == skillIdNorAtk){ 
                            // Upgrade skill
                            avatar.getSkillLevelMap().put(skillIdNorAtk, nextLevel);
                            avatar.save();
                
                            // Packet
                            sender.sendPacket(new PacketAvatarSkillChangeNotify(avatar, skillIdNorAtk, currentLevelNorAtk, nextLevel));
                            sender.sendPacket(new PacketAvatarSkillUpgradeRsp(avatar, skillIdNorAtk, currentLevelNorAtk, nextLevel));
                            CommandHandler.sendMessage(sender, "Set talent Normal ATK to " + nextLevel + ".");
                        }    
                        if (skillId == skillIdE){ 
                            // Upgrade skill
                            avatar.getSkillLevelMap().put(skillIdE, nextLevel);
                            avatar.save();
                
                            // Packet
                            sender.sendPacket(new PacketAvatarSkillChangeNotify(avatar, skillIdE, currentLevelE, nextLevel));
                            sender.sendPacket(new PacketAvatarSkillUpgradeRsp(avatar, skillIdE, currentLevelE, nextLevel));
                            CommandHandler.sendMessage(sender, "Set talent E to " + nextLevel + ".");
                        }
                        if (skillId == skillIdQ){ 
                            // Upgrade skill
                            avatar.getSkillLevelMap().put(skillIdQ, nextLevel);
                            avatar.save();
                
                            // Packet
                            sender.sendPacket(new PacketAvatarSkillChangeNotify(avatar, skillIdQ, currentLevelQ, nextLevel));
                            sender.sendPacket(new PacketAvatarSkillUpgradeRsp(avatar, skillIdQ, currentLevelQ, nextLevel));
                            CommandHandler.sendMessage(sender, "Set talent Q to " + nextLevel + ".");
                        }       
                                
                    } catch (NumberFormatException ignored) {
                        CommandHandler.sendMessage(sender, "Invalid skill ID.");
                        return;
                    }
                
                break;
            case "getid":           
                    EntityAvatar entity = sender.getTeamManager().getCurrentAvatarEntity();
                    GenshinAvatar avatar = entity.getAvatar(); 
                    int skillIdNorAtk = avatar.getData().getSkillDepot().getSkills().get(0);
                    int skillIdE = avatar.getData().getSkillDepot().getSkills().get(1);
                    int skillIdQ = avatar.getData().getSkillDepot().getEnergySkill();
                    
                    CommandHandler.sendMessage(sender, "Normal Attack ID " + skillIdNorAtk + ".");
                    CommandHandler.sendMessage(sender, "E skill ID " + skillIdE + ".");
                    CommandHandler.sendMessage(sender, "Q skill ID " + skillIdQ + ".");
                break;
        }
    }
}
