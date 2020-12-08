package com.codecool.bfsexample;

import com.codecool.bfsexample.model.UserNode;

import java.util.*;

public class Graph {

    static class NodeHistory {
        public final UserNode currentNode;
        public final UserNode previousNode;
        public final Integer distance;

        public NodeHistory(UserNode currentNode, Integer distance) {
            this.currentNode = currentNode;
            this.previousNode = null;
            this.distance = distance;
        }

        public NodeHistory(UserNode currentNode, UserNode previousNode, Integer distance) {
            this.currentNode = currentNode;
            this.previousNode = previousNode;
            this.distance = distance;
        }
    }

    private final List<UserNode> users;

    public Graph() {
        RandomDataGenerator generator = new RandomDataGenerator();
        users = generator.generate();
    }

    public List<UserNode> getUsers() {
        return users;
    }

    public UserNode getUserByID(int id) {
        return users.get(id);
    }

    public Integer getDistanceBetweenUsers(UserNode startNode, UserNode endNode) {
        Queue<NodeHistory> toVisit = new LinkedList<>();
        Queue<NodeHistory> visited = new LinkedList<>();
        toVisit.add(new NodeHistory(startNode, 0));
        while (!toVisit.isEmpty()) {
            NodeHistory visiting = toVisit.poll();
            if (isNextNodeTheDestination(endNode, visiting.currentNode))
                return visiting.distance;
            collectFriendsToVisit(toVisit, visited, visiting);
        }
        return null;
    }

    public Set<UserNode> getFriendOfFriendsByDistance(UserNode startNode, int distance) {
        Queue<NodeHistory> toVisit = new LinkedList<>();
        Queue<NodeHistory> visited = new LinkedList<>();
        Set<UserNode> friendOfFriens = new HashSet<>();
        toVisit.add(new NodeHistory(startNode, 0));
        while (!toVisit.isEmpty()) {
            NodeHistory visiting = toVisit.poll();
            if (isOutOfDistance(distance, visiting)) return friendOfFriens;
            collectFriendsToVisit(toVisit, visited, visiting);
            if (visiting.currentNode != startNode) friendOfFriens.add(visiting.currentNode);
        }
        return null;
    }

    public List<List<UserNode>> getSortestPathBetweenUsers(UserNode startNode, UserNode endNode) {
        Integer destinationDistance = null;
        Queue<NodeHistory> toVisit = new LinkedList<>();
        Queue<NodeHistory> visited = new LinkedList<>();
        List<List<UserNode>> resultPaths = new ArrayList<>();
        toVisit.add(new NodeHistory(startNode, null, 0));
        while (!toVisit.isEmpty()) {
            NodeHistory visiting = toVisit.poll();
            if (isOutOfDistance(destinationDistance, visiting))
                return resultPaths;
            if (isNextNodeTheDestination(endNode, visiting.currentNode)) {
                destinationDistance = visiting.distance;
                resultPaths.add(getPathHistory(visited, visiting));
            }
            collectFriendsToVisitWithMultipleEnd(toVisit, visited, visiting, endNode);
        }
        return null;
    }

    private void collectFriendsToVisit(Queue<NodeHistory> toVisit, Queue<NodeHistory> visited, NodeHistory visiting) {
        Set<UserNode> friends = visiting.currentNode.getFriends();
        for (UserNode friend: friends) {
            if (isNotInToVisitOrVisited(toVisit, visited, friend)) {
                toVisit.add(new NodeHistory(friend, visiting.currentNode, visiting.distance + 1));
            }
        }
        visited.add(visiting);
    }

    private void collectFriendsToVisitWithMultipleEnd(Queue<NodeHistory> toVisit, Queue<NodeHistory> visited, NodeHistory visiting, UserNode endNode) {
        Set<UserNode> friends = visiting.currentNode.getFriends();
        for (UserNode friend: friends) {
            if (isNotInToVisitOrVisited(toVisit, visited, friend) || friend.equals(endNode)) {
                toVisit.add(new NodeHistory(friend, visiting.currentNode, visiting.distance + 1));
            }
        }
        visited.add(visiting);
    }

    private boolean isNotInToVisitOrVisited(Queue<NodeHistory> toVisit, Queue<NodeHistory> visited, UserNode friend) {
        return toVisit.stream().noneMatch(e -> e.currentNode.equals(friend)) ||
                visited.stream().noneMatch(e -> e.currentNode.equals(friend));
    }

    private ArrayList<UserNode> getPathHistory(Queue<NodeHistory> visited, NodeHistory visiting) {
        ArrayList<UserNode> path = new ArrayList<>();
        NodeHistory nodeHistory = visiting;
        while (true) {
            path.add(0, nodeHistory.currentNode);
            if (nodeHistory.previousNode == null) break;
            nodeHistory = getPreviousNodeHistory(visited, nodeHistory.previousNode);
        }
        return path;
    }

    private NodeHistory getPreviousNodeHistory(Queue<NodeHistory> visited, UserNode node) {
        return visited.stream()
                .filter(e -> e.currentNode.equals(node))
                .findFirst()
                .get();
    }

    private boolean isNextNodeTheDestination(UserNode endNode, UserNode currentNode) {
        return currentNode.equals(endNode);
    }

    private boolean isOutOfDistance(Integer destinationDistance, NodeHistory visiting) {
        return destinationDistance != null && visiting.distance > destinationDistance;
    }

}
