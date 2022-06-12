package fr.varowz.hideandseek.listerners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.varowz.hideandseek.AutoStart;
import fr.varowz.hideandseek.EndGame;
import fr.varowz.hideandseek.GState;
import fr.varowz.hideandseek.Main;

public class onQuit implements Listener {
	
	Main main;
	
	public onQuit(Main main) {
		this.main=main;
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		//ScoreboardManager.scoreboardGame.remove(player);
		
		if(player == main.getSeekers()) {
			AutoStart.timer = 0;
		}
		if(main.isState(GState.ENDGAME)) {
			player.setGameMode(GameMode.SURVIVAL);
		}
		if(main.isState(GState.PLAYING)) {
		if(main.getPlayers().contains(player)) {
			player.setGameMode(GameMode.SURVIVAL);
			for(int i = 0; i < main.getPlayers().size(); i++) {
				if(player == main.getPlayers().get(i)) {
					main.setOnLife(main.getOnLife()-1);
					main.getPlayers().remove(player);
					if(main.getOnLife()==0) {
						Bukkit.broadcastMessage(main.getConfig().getString("Messages.SeekersWin").replace("&", "§"));
						//ScoreboardManager.loadOut();
						for(int ii = 0; ii < main.getPlayers().size(); ii++) {
							Player playerr = main.getPlayers().get(ii);
							playerr.setGameMode(GameMode.SURVIVAL);
							Main.boards.get(playerr.getUniqueId()).delete();
						}
						Main.boards.get(main.getSeekers().getUniqueId()).delete();
						EndGame end = new EndGame(main, main.getSeekers().getLocation());
						end.runTaskTimer(main, 0, 20);
						Bukkit.getScheduler().cancelAllTasks();
						main.setState(GState.ENDGAME);
						
						
					}
				}
			}
		}}
	}
}
