package fr.varowz.hideandseek;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import utils.TitleManager;

public class AutoStart extends BukkitRunnable{

	public static int timer;
	private Main main;
	
	public AutoStart(Main main) {

		this.main = main;
	}

	@Override
	public void run() {
		
		if(timer == 0) {
			//fin du jeu
			main.setState(GState.ENDGAME);
			Bukkit.broadcastMessage(main.getConfig().getString("Messages.HiderWin").replace("&", "§"));
			//ScoreboardManager.loadOut();
			for(int i = 0; i < main.getPlayers().size(); i++) {
				Player player = main.getPlayers().get(i);
				player.setGameMode(GameMode.SURVIVAL);
				Main.boards.get(player.getUniqueId()).delete();
			}
			cancel();
			Main.boards.get(main.getSeekers().getUniqueId()).delete();
			EndGame end = new EndGame(main, main.getPlayers().get(0).getLocation());
			end.runTaskTimer(main, 0, 20);
			
			
		}
		if(timer == 60 || timer == 10 || timer == 5 || timer == 4 || timer == 3 || timer == 2 || timer == 1) {
			Bukkit.broadcastMessage(main.getPrefix()+main.getConfig().getString("Messages.TimerAnnoncement")
					.replace("%timer%", timer+"")
					.replace("&", "§"));
		}
		for(int i = 0; i < main.getPlayers().size(); i++) {
			Player player = main.getPlayers().get(i);
			TitleManager.sendActionBar(player, "§3"+Timer(timer));
			Main.boards.get(player.getUniqueId()).updateLine(3, "§c§lTEMPS §7" + AutoStart.Timer(AutoStart.timer));
			Main.boards.get(player.getUniqueId()).updateLine(5, "§c§lEN VIE §7" + main.getOnLife());
			Main.boards.get(player.getUniqueId()).updateLine(4, "§c§lCHERCHEUR §7" + main.getSeekers().getName());
			/*if(ScoreboardManager.scoreboardGame.containsKey(player)) {
				ScoreboardManager.scoreboardGame.get(player).setLine(8, "§c§lTEMPS §7" + Timer(timer));
				ScoreboardManager.scoreboardGame.get(player).setLine(7, "§c§lCHERCHEUR §7" + main.getSeekers().getName());
			}*/
		}
		TitleManager.sendActionBar(main.getSeekers(), "§3"+Timer(timer));
		Main.boards.get(main.getSeekers().getUniqueId()).updateLine(3, "§c§lTEMPS §7" + AutoStart.Timer(AutoStart.timer));
		Main.boards.get(main.getSeekers().getUniqueId()).updateLine(5, "§c§lEN VIE §7" + main.getOnLife());
		Main.boards.get(main.getSeekers().getUniqueId()).updateLine(4, "§c§lCHERCHEUR §7" + main.getSeekers().getName());
		/*if(ScoreboardManager.scoreboardGame.containsKey(main.getSeekers())) {
			ScoreboardManager.scoreboardGame.get(main.getSeekers()).setLine(8, "§c§lTEMPS §7" + Timer(timer));
			ScoreboardManager.scoreboardGame.get(main.getSeekers()).setLine(7, "§c§lCHERCHEUR §7" + main.getSeekers().getName());
		}*/
		timer--;
	}
	
	public static String Timer(int s) {
		int seconde = s%60;
		int min= (s/60)%60;
		if(seconde==9||seconde==8||seconde==7||seconde==6||seconde==5||seconde==4||seconde==3||seconde==2||seconde==1||seconde==0) {
			String c = "0"+seconde;
			return "0"+min + ":"+ c;
		}
		else {
			return "0"+min + ":"+ seconde;
		}
	}
}
