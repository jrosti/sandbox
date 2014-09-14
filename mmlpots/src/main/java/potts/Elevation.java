package potts;

public class Elevation {

    public static final double ASC_LIMIT = 0.005;

    private final double[] distances;
    private final double[] elevations;

    private int[] spins;

    public static int[] solve(double[] distances, double[] elevations) {
        return new Elevation(distances, elevations).solveSpins();
    }

    Elevation(double[] distances, double[] elevations) {
        this.distances = distances;
        this.elevations = elevations;
    }

    int[] solveSpins() {
        initialize();
        return spins;
    }

    void initialize() {
        spins = new int[elevations.length];
        for (int i=0; i < spins.length; i++) {
            double ascensionRate = elevations[i] / distances[i];
            if (ascensionRate < -ASC_LIMIT) {
                spins[i] = -1;
            } else if (ascensionRate > ASC_LIMIT){
                spins[i] = 1;
            } else {
                spins[i] = 0;
            }
        }
    }

}
