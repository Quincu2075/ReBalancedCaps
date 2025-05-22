package me.reBalancedCaps;

import me.angeschossen.lands.api.LandsIntegration;
import me.angeschossen.lands.api.events.land.block.LandBlockPlaceEvent;
import me.angeschossen.lands.api.land.Container;
import me.angeschossen.lands.api.land.Land;
import me.angeschossen.lands.api.land.block.LandBlockType;
import me.angeschossen.lands.api.memberholder.HolderType;
import me.angeschossen.lands.api.memberholder.MemberHolder;
import me.angeschossen.lands.api.nation.Nation;
import me.angeschossen.lands.api.war.War;
import me.angeschossen.lands.api.war.captureflag.CaptureFlag;
import me.angeschossen.lands.api.war.enums.WarTeam;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.Callable;

public final class ReBalancedCaps extends JavaPlugin implements Listener {

    public static final int MINIMUM_SECONDS = 35;
    public static final int MAXIMUM_SECONDS = 150;
    private LandsIntegration landsIntegration;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.landsIntegration = LandsIntegration.of(this);
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onLandBlockPlace(LandBlockPlaceEvent event) {
        if (event.getLandBlock().getType() != LandBlockType.CAPTURE_FLAG) return;
        CaptureFlag captureFlag = (CaptureFlag) event.getLandBlock();
        captureFlag.setEvaluateCapturingTeam(new Callable<WarTeam>() {
            @Override
            public WarTeam call() throws Exception {
                return WarTeam.ATTACKER; //always the invader
            }
        });

        captureFlag.setSecondsToHold(calcSecondsPerCap(calcChunks(getOpposing(captureFlag.getPlacedByTeam(), captureFlag.getWar()))));
    }

    private MemberHolder getOpposing(WarTeam warTeam, War war){
        if (warTeam == WarTeam.ATTACKER){
            return war.getDefender();
        }else {
            return war.getAttacker();
        }
    }

    private int calcChunks(MemberHolder memberHolder){
        if (memberHolder.getType() == HolderType.LAND){
            int total = 0;
            for (Container c : landsIntegration.getLandByName(memberHolder.getName()).getContainers()){
                total += c.getChunks().size();
            }
            return total;
        }else {
            Nation nation = landsIntegration.getNationByName(memberHolder.getName());
            int total = 0;
            for (Land l : nation.getLands()){
                for (Container c : l.getContainers()){
                    total += c.getChunks().size();
                }
            }
            return total;
        }
    }

    private int calcSecondsPerCap(int opposingChunks){
        int secondsToAdd = (int)(0.6 * opposingChunks);

        int mod = secondsToAdd % 5;
        if (mod != 0){
            secondsToAdd -= mod;
            secondsToAdd += 5;
        }

        int total = secondsToAdd + MINIMUM_SECONDS;
        if (total > MAXIMUM_SECONDS) total = MAXIMUM_SECONDS;

        return total;
    }
}
