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
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    int n = scanner.nextInt();
    int t = scanner.nextInt();
    Map<Integer, Point> positions = new HashMap<>();

    double actualLen = findLen(positions, scanner, n);
    double gpsLen = findGpsLen(positions, t, n);

    System.out.printf("%.5f", 100 - 100 * gpsLen / actualLen);
  }

  public static double findLen(Map<Integer, Point> positions, Scanner scanner, int n) {
    double actualLen = 0.0;
    int last = -1;
    for (int i = 0; i < n; i++) {
      int x = scanner.nextInt();
      int y = scanner.nextInt();
      int t = scanner.nextInt();
      positions.put(t, new Point(x, y));
      if (last != -1) {
        actualLen += positions.get(last).distanceTo(positions.get(t));
      }
      last = t;
    }
    return actualLen;
  }

  public static int binarySearchLeastNotSmaller(List<Integer> sortedTimes, int gpsTime) {
    int l = 0, r = sortedTimes.size() - 1;
    while (l <= r) {
      int m = l + (r - l) / 2;
      if (sortedTimes.get(m) < gpsTime) {
        l = m + 1;
      } else {
        r = m - 1;
      }
    }
    return l;
  }

  public static Point interpolatePoint(List<Integer> sortedTimes, Map<Integer, Point> positions, int gpsTime) {
    int indexAbove = binarySearchLeastNotSmaller(sortedTimes, gpsTime);
    int right = sortedTimes.get(indexAbove);
    int left = sortedTimes.get(indexAbove - 1);

    double lRatio = (double) (right - gpsTime) / (right - left);
    double rRatio = 1 - lRatio;

    Point leftPoint = positions.get(left);
    Point rightPoint = positions.get(right);

    double x = leftPoint.x * lRatio + rightPoint.x * rRatio;
    double y = leftPoint.y * lRatio + rightPoint.y * rRatio;

    return new Point(x, y);
  }

  public static double findGpsLen(Map<Integer, Point> positions, int t, int last) {
    List<Integer> sortedTimes = new ArrayList<>(positions.keySet());
    double gpsLen = 0.0;
    int gpsTime = t;
    Point lastCoord = positions.get(0); // always starts on 0
    boolean stopLoop = false;
    while (!stopLoop) {
      if (gpsTime >= last) {
        gpsTime = last;
        stopLoop = true;
      }

      if (positions.containsKey(gpsTime)) {
        gpsLen += lastCoord.distanceTo(positions.get(gpsTime));
        lastCoord = positions.get(gpsTime);
      } else {
        Point tmp = interpolatePoint(sortedTimes, positions, gpsTime);
        gpsLen += lastCoord.distanceTo(tmp);
        lastCoord = tmp;
      }

      gpsTime += t;
    }
    return gpsLen;
  }
}
