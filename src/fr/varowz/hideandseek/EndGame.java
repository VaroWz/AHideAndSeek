package fr.varowz.hideandseek;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class EndGame extends BukkitRunnable{

	private Main main;
	int timer = 10;
	private Location loc;
			
	public EndGame(Main main, Location loc) {
		this.main=main;
		this.loc=loc;
	}
	
	@Override
	public void run() {
		
		if(timer==0) {
			main.setState(GState.NOGAME);
			for(String string: main.getConfig().getConfigurationSection("Events").getKeys(false)) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), main.getConfig().getConfigurationSection("EndCommands").getString(string));
			}
			
			main.getPlayers().clear();
			main.setSeekers(null);
			cancel();
		}
		Firework fw = (Firework)loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
		FireworkMeta fwm = fw.getFireworkMeta();
		fwm.setPower(1);
		fwm.addEffect(FireworkEffect.builder().withColor(Color.RED).withColor(Color.BLUE).with(Type.BALL_LARGE).build());
		fw.setFireworkMeta(fwm);
		
		timer--;
		
		
	}

}
