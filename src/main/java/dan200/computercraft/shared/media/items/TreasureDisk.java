package dan200.computercraft.shared.media.items;

public record TreasureDisk(String title, String subPath, int colour) {
    public static TreasureDisk[] DISKS = new TreasureDisk[]{
        new TreasureDisk("gameoflife by vilsol", "vilsol/gameoflife", 15905331),
        new TreasureDisk("maze3d by jtk", "jtk/maze3d", 3368652),
        new TreasureDisk("nsh by lyqyd", "lyqyd/nsh", 14605932),
        new TreasureDisk("npaintpro by nitrogenfingers", "nitrogenfingers/npaintpro", 3368652),
        new TreasureDisk("goldrunner by nitrogenfingers", "nitrogenfingers/goldrunner", 15040472),
        new TreasureDisk("protector by fredthead", "fredthead/protector", 15790320),
        new TreasureDisk("luaide by gravity_score", "gravity_score/luaide", 10066329),
        new TreasureDisk("tictactoe by theoriginalbit", "theoriginalbit/tictactoe", 1118481),
        new TreasureDisk("battleship by gopher_atl", "gopher_atl/battleship", 11691749),
        new TreasureDisk("alongtimeago by dan200", "dan200/alongtimeago", 14605932)
    };

}

