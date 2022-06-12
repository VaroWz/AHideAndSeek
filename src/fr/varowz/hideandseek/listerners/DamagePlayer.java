package fr.varowz.hideandseek.listerners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import fr.varowz.hideandseek.EndGame;
import fr.varowz.hideandseek.GState;
import fr.varowz.hideandseek.Main;

public class DamagePlayer implements Listener {

	private Main main;
	
	public DamagePlayer(Main main) {
		this.main = main;
	}
	
	@EventHandler
	public void onPvp(EntityDamageByEntityEvent event) {
		
		if(event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
			
			if(main.isState(GState.PLAYING)) {
				Player player = (Player) event.getDamager(); 
				Player victim = (Player) event.getEntity();
				event.setCancelled(true);
				if(main.getSeekers() == player) {
					
					for(int i = 0; i < main.getPlayers().size(); i++) {
						
						if(main.getPlayers().get(i) == victim) {
							main.setOnLife(main.getOnLife()-1);
							Bukkit.broadcastMessage(main.getConfig().getString("Messages.FoundPlayer")
									.replace("&", "§")
									.replace("%player%", victim.getName()));
							victim.setGameMode(GameMode.SPECTATOR);
						}
					}
					if(main.getOnLife()==0) {
						Bukkit.getScheduler().cancelAllTasks();
						Bukkit.broadcastMessage(main.getConfig().getString("Messages.SeekersWin").replace("&", "§"));
						
						//ScoreboardManager.loadOut();
						for(int i = 0; i < main.getPlayers().size(); i++) {
							Player playerr = main.getPlayers().get(i);
							playerr.setGameMode(GameMode.SURVIVAL);
							Main.boards.get(playerr.getUniqueId()).delete();
						}
						Main.boards.get(main.getSeekers().getUniqueId()).delete();
						
						main.setState(GState.ENDGAME);
						EndGame end = new EndGame(main, player.getLocation());
						end.runTaskTimer(main, 0, 20);
					}
				}
			}
			else {
				return;
			}
		}
	}
}
