package dev.gllody.meteorcapes.modules;

import dev.gllody.meteorcapes.MeteorCapes;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.meteorclient.settings.EnumSetting;

public class CapesModule extends Module {
    public CapesModule() {super(MeteorCapes.CATEGORY, "capes", "Just pick any cape (you need to rejoin)");}
    public static String capeed;
    private final SettingGroup sgGeneral1 = settings.getDefaultGroup();
    public final Setting<Mode> modee = sgGeneral1.add(new EnumSetting.Builder<Mode>()
        .name("mode")
        .description("Decide from packet or client sided rotation.")
        .defaultValue(Mode.Aetheric)
        .build()
    );
    public enum Mode {
        Aetheric("Aetheric"),
        Avo("AVO"),
        Anime1("Anime1"),
        Anime2("Anime2"),
        Anime3("Anime3"),
        Anime4("Anime4"),
        Clown("Clown"),
        Developer("DEV"),
        Feather("Feather"),
        VapeV4("VapeV4"),
        hacker("Hacker"),
        Anarchy("Anarchy"),
        Minecon2011("2011"),
        Minecon2012("2012"),
        Minecon2013("2013"),
        Minecon2015("2015"),
        Minecon2016("2016");
        private final String title;

        Mode(String title) {
            this.title = title;
        }
        @Override
        public String toString() {
            return title;
        }
    }
    @EventHandler
    public void onActivate() {
        Mode selectedMode = modee.get();
        switch (selectedMode) {
            case Aetheric:
                capeed = "aetheric";
                break;
            case Avo:
                capeed = "avo";
                break;
            case Anime1:
                capeed = "anime1";
                break;
            case Anime2:
                capeed = "anime2";
                break;
            case Anime3:
                capeed = "anime3";
                break;
            case Anime4:
                capeed = "anime4";
                break;
            case Feather:
                capeed = "feather";
                break;
            case hacker:
                capeed = "hacker";
                break;
            case VapeV4:
                capeed = "vapev";
                break;
            case Anarchy:
                capeed = "anarchy";
                break;
            case Clown:
                capeed = "clow";
                break;
            case Developer:
                capeed = "dev";
                break;
            case Minecon2011:
                capeed = "2011";
                break;
            case Minecon2012:
                capeed = "2012";
                break;
            case Minecon2013:
                capeed = "2013";
                break;
            case Minecon2015:
                capeed = "2015";
                break;
            case Minecon2016:
                capeed = "2016";
                break;
            default:
                capeed = "anime2";
                break;
        }
    }
    @EventHandler
    public void onDeactivate() {
        capeed = null;
    }
}
