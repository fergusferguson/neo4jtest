import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.types.Node;
import org.neo4j.driver.v1.types.Path;
import org.neo4j.driver.v1.types.Relationship;
import org.neo4j.driver.v1.util.Pair;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class neo4jtest {

    public static void main(String[] args) {
        Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "password"));

        try (Session session = driver.session()) {

            String query = "match path=((depot:Depot{depotName:\"Daventry Clothing\"})-[r:FRONT_HAUL*1..5]->(store:Store{storeName:\"Skipton\"})) return path";
            System.out.println("Cypher query : " + query);
            System.out.println();

            try (Transaction tx = session.beginTransaction()) {
                StatementResult result = tx.run(
                    "match path=((depot:Depot{depotName:\"Daventry Clothing\"})-[r:FRONT_HAUL*1..5]->(store:Store{storeName:\"Skipton\"})) return path");

                while (result.hasNext()) {

                    Record record = result.next();

                    System.out.println("record.toString()");
                    System.out.println(record.toString());
                    System.out.println();

                    List<Pair<String, Value>> fields = record.fields();

                    System.out.println("fields.toString()");
                    System.out.println(fields.toString());
                    System.out.println();

                    for (Pair<String, Value> pair : fields) {

                        System.out.println("pair.value().toString()");
                        System.out.println(pair.value().toString());
                        System.out.println();

                        // Get the list of segments in the record and iterate over them
                        Iterator<Path.Segment> seg = pair.value().asPath().iterator();
                        while (seg.hasNext()){
                            Path.Segment s = seg.next();
//                            System.out.println(s);

                            // get the start node, find the labels and get the node properties
                            Node startNode = s.start();
                            Map<String, Object> startNodeMap = startNode.asMap();
                            System.out.println();
                            System.out.println("START NODE:");
                            System.out.println(startNode.labels());
                            for (Map.Entry<String, Object> startNodeEntry : startNodeMap.entrySet()){
                                System.out.println(startNodeEntry.getKey() + " : " + startNodeEntry.getValue().toString());
                            }

                            // get the relationship, find its type and its properties
                            System.out.println();
                            System.out.println("RELATIONSHIP:");
                            Relationship relationship = s.relationship();
                            System.out.println(relationship.type());
                            Map<String, Object> relationshipMap = relationship.asMap();
                            for (Map.Entry<String, Object> relationshipEntry : relationshipMap.entrySet()){
                                System.out.println(relationshipEntry.getKey() + " : " + relationshipEntry.getValue().toString());
                            }

                            // get the end node, find the labels and get the node properties
                            Node endNode = s.end();
                            Map<String, Object> endNodeMap = endNode.asMap();
                            System.out.println();
                            System.out.println("END NODE:");
                            System.out.println(endNode.labels());
                            for (Map.Entry<String, Object> endNodeEntry : endNodeMap.entrySet()){
                                System.out.println(endNodeEntry.getKey() + " : " + endNodeEntry.getValue().toString());
                            }
                        }

                        // Get all the relationships in the path
//                        System.out.println();
//                        System.out.println("GET RELATIONSHIPS");
//                        Iterable<Relationship> rels = pair.value().asPath().relationships();
//
//                        for (Relationship r : rels){
//                            String type = r.type();
//                            System.out.println("Rel type : " + type);
//                            Map<String, Object> relMap = r.asMap();
//
//                            for (Map.Entry<String, Object> entry : relMap.entrySet()) {
//                                System.out.println(entry.getKey() + " : " + entry.getValue().toString());
//                            }
//                            System.out.println();
//                        }
//
//                        System.out.println();
//
//                         Get all the nodes in the path
//                        System.out.println("GET NODES");
//                        Iterable<Node> nodes = pair.value().asPath().nodes();
//
//                        for (Node n : nodes) {
//                            Iterable<String> labels = n.labels();
//                            System.out.println("Labels : " + labels);
//                            Map<String, Object> nodeMap = n.asMap();
//
//                            for (Map.Entry<String, Object> entry : nodeMap.entrySet()) {
//                                System.out.println(entry.getKey() + " : " + entry.getValue().toString());
//                            }
//                            System.out.println();
//
//                        }
                    }
                }
            }
        }
        driver.close();
    }
}
