import java.util.*;

class Point {
  double x;
  double y;

  public Point(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public double distanceTo(Point other) {
    return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
  }

}

public class problemC {
  static double calculateActualLength(Map<Integer, Point> positions, int n, Scanner scanner) {
    double actualLength = 0.0;
    int lastTime = -1;
    for (int i = 0; i < n; i++) {
      double x = scanner.nextInt();
      double y = scanner.nextInt();
      int time = scanner.nextInt();
      positions.put(time, new Point(x, y));
      if (lastTime != -1) {
        actualLength += positions.get(lastTime).distanceTo(positions.get(time));
      }
      lastTime = time;
    }
    return actualLength;
  }

  static int findIndexOfLeastNotSmaller(List<Integer> sortedTimes, int length, int targetTime) {
    int left = 0;
    int right = length - 1;
    while (left <= right) {
      int mid = left + (right - left) / 2;
      if (sortedTimes.get(mid) < targetTime) {
        left = mid + 1;
      } else {
        right = mid - 1;
      }
    }
    return left;
  }

  static Point interpolatePoint(List<Integer> sortedTimes, Map<Integer, Point> positions, int gpsTime) {
    int indexAbove = findIndexOfLeastNotSmaller(sortedTimes, sortedTimes.size(), gpsTime);
    int rightTime = sortedTimes.get(indexAbove);
    int leftTime = sortedTimes.get(indexAbove - 1);

    double leftRatio = (double)(rightTime - gpsTime) / (rightTime - leftTime);
    double rightRatio = 1 - leftRatio;

    return new Point(
            positions.get(leftTime).x * leftRatio + positions.get(rightTime).x * rightRatio,
            positions.get(leftTime).y * leftRatio + positions.get(rightTime).y * rightRatio
    );
  }

  static double calculateGpsLength(Map<Integer, Point> positions, int timeInterval, int lastTime) {
    List<Integer> sortedTimes = new ArrayList<>(positions.keySet());
    double gpsLength = 0.0;
    int gpsTime = timeInterval;
    boolean stopLoop = false;
    Point lastCoord = positions.get(0); // always starts at time 0
    while (!stopLoop) {
      if (gpsTime >= lastTime) {
        gpsTime = lastTime;
        stopLoop = true;
      }

      if (positions.containsKey(gpsTime)) {
        gpsLength += lastCoord.distanceTo(positions.get(gpsTime));
        lastCoord = positions.get(gpsTime);
      } else {
        Point interpolatedPoint = interpolatePoint(sortedTimes, positions, gpsTime);
        gpsLength += lastCoord.distanceTo(interpolatedPoint);
        lastCoord = interpolatedPoint;
      }

      gpsTime += timeInterval;
    }
    return gpsLength;
  }

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    int numberOfPoints = scanner.nextInt();
    int timeInterval = scanner.nextInt();
    Map<Integer, Point> positions = new HashMap<>();

    double actualLength = calculateActualLength(positions, numberOfPoints, scanner);
    Map.Entry<Integer, String>[] entries = positions.entrySet().toArray(new Map.Entry[0]);

    int lastTime = entries[entries.length -1].getKey();

    double gpsLength = calculateGpsLength(positions, timeInterval, lastTime);

    System.out.printf("%.5f", 100 - 100 * gpsLength / actualLength);

    scanner.close();
  }
}
