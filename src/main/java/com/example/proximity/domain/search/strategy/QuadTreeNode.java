package com.example.proximity.domain.search.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class QuadTreeNode {
    private static final int MAX_CAPACITY = 32;
    private final Boundary boundary;
    private final List<Point> points = new ArrayList<>();
    private QuadTreeNode nw, ne, sw, se;
    private boolean divided = false;

    public record Boundary(double minLat, double maxLat, double minLon, double maxLon) {
        public boolean contains(double lat, double lon) {
            return lat >= minLat && lat <= maxLat && lon >= minLon && lon <= maxLon;
        }

        public boolean intersects(Boundary other) {
            return !(other.minLat > maxLat || other.maxLat < minLat || other.minLon > maxLon || other.maxLon < minLon);
        }
    }

    public record Point(UUID id, double lat, double lon) {
    }

    public QuadTreeNode(Boundary boundary) {
        this.boundary = boundary;
    }

    public boolean insert(Point point) {
        if (!boundary.contains(point.lat, point.lon))
            return false;

        if (points.size() < MAX_CAPACITY) {
            points.add(point);
            return true;
        }

        if (!divided)
            subdivide();

        return nw.insert(point) || ne.insert(point) || sw.insert(point) || se.insert(point);
    }

    private void subdivide() {
        double midLat = (boundary.minLat + boundary.maxLat) / 2;
        double midLon = (boundary.minLon + boundary.maxLon) / 2;

        nw = new QuadTreeNode(new Boundary(midLat, boundary.maxLat, boundary.minLon, midLon));
        ne = new QuadTreeNode(new Boundary(midLat, boundary.maxLat, midLon, boundary.maxLon));
        sw = new QuadTreeNode(new Boundary(boundary.minLat, midLat, boundary.minLon, midLon));
        se = new QuadTreeNode(new Boundary(boundary.minLat, midLat, midLon, boundary.maxLon));
        divided = true;
    }

    public void query(Boundary searchBoundary, List<Point> found) {
        if (!boundary.intersects(searchBoundary))
            return;

        for (Point p : points) {
            if (searchBoundary.contains(p.lat, p.lon)) {
                found.add(p);
            }
        }

        if (divided) {
            nw.query(searchBoundary, found);
            ne.query(searchBoundary, found);
            sw.query(searchBoundary, found);
            se.query(searchBoundary, found);
        }
    }
}
