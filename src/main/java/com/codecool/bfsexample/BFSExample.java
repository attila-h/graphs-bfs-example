package com.codecool.bfsexample;

import com.codecool.bfsexample.model.UserNode;

public class BFSExample {

    public static void main(String[] args) {
        Graph graph = new Graph();
        GraphPlotter graphPlotter = new GraphPlotter(graph.getUsers());
        UserNode user5 = graph.getUserByID(5);
        UserNode user10 = graph.getUserByID(10);

        graphPlotter.highlightNode(user5);
        graphPlotter.highlightNode(user10);

        System.out.println(String.format(
                "Distance between %s and %s is %s",
                user5, user10, graph.getDistanceBetweenUsers(user5, user10))
        );

        UserNode user20 = graph.getUserByID(20);
        graphPlotter.highlightNodes(
                graph.getFriendOfFriendsByDistance(user20, 2),
                user20
        );

        UserNode user106 = graph.getUserByID(106);
        UserNode user51 = graph.getUserByID(51);

        graphPlotter.highlightRoute(graph.getSortestPathBetweenUsers(user106, user51));
    }


}
