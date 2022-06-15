package fr.varowz.hideandseek;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.varowz.hideandseek.scoreboard.FastBoard;
import utils.TitleManager;

public class CommandHideAndSeek implements CommandExecutor {

	private Main main;
	
	public CommandHideAndSeek(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		
		//permission requis
		if(sender.hasPermission("hideandseek.commands")) {
			
			if(args.length == 0) {
				HelpMessage(sender);
			}
			//un seul argument
			if(args.length == 1) {
				String arg = args[0];
				if(arg.equalsIgnoreCase("reload") || arg.equalsIgnoreCase("rl")) {
					
					main.setPrefix(main.getConfig().getString("Messages.Prefix").replace("&", "§"));
					main.reloadConfig();
					sender.sendMessage(main.getPrefix() + main.getConfig().getString("Messages.Reloading").replace("&", "§"));
		
				}
				//else {
					//HelpMessage(sender);
				//}
			}
			//2 ou plus d'arguments
			if(args.length == 1) {
				String arg = args[0];
				//si c'est start
				if(arg.equalsIgnoreCase("start")) {
					//si le jeu est déjà commencé
					if(!main.isState(GState.NOGAME)) {
						sender.sendMessage(main.getPrefix() + main.getConfig().getString("Messages.AlreadyStarted").replace("&", "§"));
						return false;
					}
					//clear de la partie
					main.getPlayers().clear();
					//récupère tout les joueurs en ligne
					for(Player player : Bukkit.getServer().getOnlinePlayers()) {
						//répète le nombre de fois que d'arguments
						//for (int i=1; i<args.length;i++) {
							//si le joueur de la boucle joueur est égal à un argument
							//if(player.getName().equalsIgnoreCase(args[i])) {
								//ajout du joueur dans la partie
								if(!main.getPlayers().contains(player)) {
									main.getPlayers().add(player);
									System.out.println(main.getPrefix() + player.getName() + " is added to the game.");
									
									
									FastBoard board = new FastBoard(player);
									board.updateTitle("§8§m---§a§lCACHE-CACHE§8§m---");
									board.updateLine(10, "§aplay.ratmc.fr");
									board.updateLine(9, "§7");
									board.updateLine(8, "§c§lSERVEUR §7" + main.getConfig().getString("ServerName"));
									board.updateLine(7, "           §e§lSERVEUR");
									board.updateLine(6, "§7");
									board.updateLine(5, "§c§lEN VIE §7");
									board.updateLine(4, "§c§lCHERCHEUR §7");
									board.updateLine(3, "§c§lTEMPS §7" + AutoStart.Timer(AutoStart.timer));
									board.updateLine(2, "           §e§lPARTIE");
									board.updateLine(1, "§7");
									
									Main.boards.put(player.getUniqueId(), board);
									
									
									
									//if(!ScoreboardManager.scoreboardGame.containsKey(player)) {
									//	new ScoreboardManager(player, main).loadScoreboard();
									//}
								}
								//le joueur existe deja dans la partie
								else {
									System.out.println(main.getPrefix()+player.getName()+ " already exists in the game.");
								}
							//}
							//le joueur a pas un pseudo de qlq de co
							//else {
								//System.out.println(main.getPrefix() + args[i] + " is not a player.");
							//}
						//}
					}
					//si il n'y a pas assez de joueur
					if(main.getPlayers().isEmpty() || main.getPlayers().size() == 1) {
						sender.sendMessage(main.getPrefix() + main.getConfig().getString("Messages.EnoughPlayer").replace("&", "§"));
						System.out.println(main.getPrefix() + "Too few players to start the game.");
						main.getPlayers().clear();
						return true;
					}
					
					//chiffre random entre 0 et le nombre de joueur 
					Random r = new Random();
					//si y'a deux valeurs ça retourne 0 ou 1
					int alea = r.nextInt(main.getPlayers().size());
					//retire un joueur aléatoire des cacheurs et le met en seekers
					main.setSeekers(main.getPlayers().get(alea));
					main.getPlayers().remove(alea);
					//annonce le seekers
					Bukkit.broadcastMessage(main.getPrefix()+main.getConfig().getString("Messages.BroadcastSeekers")
							.replace("%player%", main.getSeekers().getName())
							.replace("&", "§"));
					//tp le seekers
					Location loc = new Location(main.getSeekers().getWorld(),
							main.getConfig().getInt("Seekers.x"), 
							main.getConfig().getInt("Seekers.y"), 
							main.getConfig().getInt("Seekers.z"));
					main.getSeekers().teleport(loc);
					//répète le nombre de fois de joueur dans les hiders
					for(int i = 0; i < main.getPlayers().size(); i++) {
						Player player = main.getPlayers().get(i);
						
						Location locc = new Location(player.getWorld(), 
								main.getConfig().getInt("Hider.x"), 
								main.getConfig().getInt("Hider.y"), 
								main.getConfig().getInt("Hider.z"));
						player.teleport(locc);
						
					}
					//sattut en jeu
					main.setState(GState.PLAYING);
					//ajout title pour les joueurs cacheur
					for(int i = 0; i < main.getPlayers().size(); i++) {
						Player player = main.getPlayers().get(i);
						TitleManager.sendTitle(player, 
								main.getConfig().getString("Messages.TitleHider")
								.replace("%seekers%", main.getSeekers().getName())
								.replace("&", "§"), 
								main.getConfig().getString("Messages.SublineTitleHider")
								.replace("%seekers%", main.getSeekers().getName())
								.replace("&", "§"), 80);
						//if(!ScoreboardManager.scoreboardGame.containsKey(player)) {
							//new ScoreboardManager(player, main).loadScoreboard();
						//}
					}
					//if(!ScoreboardManager.scoreboardGame.containsKey(main.getSeekers())) {
						//new ScoreboardManager(main.getSeekers(), main).loadScoreboard();
					//}
					//title pour le chercheur
					TitleManager.sendTitle(main.getSeekers(),
							main.getConfig().getString("Messages.TitleSeekers")
							.replace("%seekers%", main.getSeekers().getName())
							.replace("&", "§"), 
							main.getConfig().getString("Messages.SublineTitleSeekers")
							.replace("%seekers%", main.getSeekers().getName())
							.replace("&", "§"), 80);
					
					
					main.setOnLife(main.getPlayers().size());
					main.getSeekers().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*30, 5 -1));
					AutoStart.timer = 330;
					AutoStart start = new AutoStart(main);
					start.runTaskTimer(main, 0, 20);
					
					
				}
			}
		}
		else {
			sender.sendMessage(main.getConfig().getString("Messages.NoPerm").replace("&", "§"));
		}
		
		return false;
	}
	public void HelpMessage(CommandSender sender) {
		sender.sendMessage("§8§m-----"+main.getPrefix()+"§8§m-----");
		sender.sendMessage("§6/hideandseek reload §ePour reload la config.");
		sender.sendMessage("§6/hideandseek start §eLancer la partie.");
	}

}
