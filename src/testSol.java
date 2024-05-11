import java.util.*;

public class testSol {

  static class Point {
    double x;
    double y;
    double t;

    public Point(double x, double y, double t) {
      this.x = x;
      this.y = y;
      this.t = t;
    }
  }

  static double calculateDistance(List<Point> pos) {
    double total = 0;
    for (int i = 1; i < pos.size(); i++) {
      Point a = pos.get(i);
      Point b = pos.get(i - 1);
      total += Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }
    return total;
  }

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    int n = scanner.nextInt();
    int t = scanner.nextInt();

    List<Point> positions = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      double x = scanner.nextDouble();
      double y = scanner.nextDouble();
      double time = scanner.nextDouble();
      positions.add(new Point(x, y, time));
    }

    List<Point> gpsPositions = new ArrayList<>();
    gpsPositions.add(positions.get(0));
    double maxT = positions.get(positions.size() - 1).t;
    int curIndex = 1;
    double gpsT = t;
    while (gpsT < maxT + 1) {
      double curT = positions.get(curIndex).t;
      if (gpsT <= curT) {
        double percentage = (gpsT - positions.get(curIndex - 1).t) / (curT - positions.get(curIndex - 1).t);
        double newX = ((positions.get(curIndex).x - positions.get(curIndex - 1).x) * percentage) + positions.get(curIndex - 1).x;
        double newY = ((positions.get(curIndex).y - positions.get(curIndex - 1).y) * percentage) + positions.get(curIndex - 1).y;
        gpsPositions.add(new Point(newX, newY, gpsT));
        gpsT += t;
      } else {
        curIndex++;
      }
    }
    gpsPositions.add(positions.get(positions.size() - 1));

    double real = calculateDistance(positions);
    double gps = calculateDistance(gpsPositions);
    System.out.println((real - gps) * 100 / real);
  }
}

