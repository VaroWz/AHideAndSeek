package fr.varowz.hideandseek;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.varowz.hideandseek.listerners.DamagePlayer;
import fr.varowz.hideandseek.listerners.onQuit;
import fr.varowz.hideandseek.scoreboard.FastBoard;

public class Main extends JavaPlugin{
	
	//scoreboard
	public static Map<UUID, FastBoard> boards = new HashMap<>();
	
	
	private String prefix;
	private GState state;
	private ArrayList<Player> players = new ArrayList<>();
	private Player seekers;
	private int onLife;
	
	@Override
	public void onEnable() {
		setPrefix(getConfig().getString("Messages.Prefix").replace("&", "§"));
		
		saveDefaultConfig();
				
		setState(GState.NOGAME);
		getCommand("HideAndSeek").setExecutor(new CommandHideAndSeek(this));
		
		getServer().getPluginManager().registerEvents(new DamagePlayer(this), this);
		getServer().getPluginManager().registerEvents(new onQuit(this), this);
		
		
		Bukkit.getConsoleSender().sendMessage(getPrefix() + "§aPlugin §7[§e1.0.0§7] §ais online.");
		Bukkit.getConsoleSender().sendMessage(getPrefix() + "§aPlugin §7[§e1.0.0§7] §cDev by VaroWz.");
		
		
	}
	@Override
	public void onDisable() {
		
		Bukkit.getConsoleSender().sendMessage(getPrefix() + "§aPlugin §7[§e1.0.0§7] §cis offline.");
		
	}
	public void setState(GState state) {
		this.state = state;
	}
	public boolean isState(GState state) {
		return this.state == state;
	}
	public ArrayList<Player> getPlayers() {
		return players;
	}
	public Player getSeekers() {
		return seekers;
	}
	public void setSeekers(Player seekers) {
		this.seekers = seekers;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public int getOnLife() {
		return onLife;
	}
	public void setOnLife(int onLife) {
		this.onLife = onLife;
	}
	

}
