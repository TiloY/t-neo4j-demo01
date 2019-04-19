package com.ty.neo4j.test;

import java.util.HashMap;
import java.util.Map;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import com.ty.neo4j.label.MyLabels;
import com.ty.neo4j.rela.RelTypes;

import com.ty.neo4j.db.DB;

public class Neo4jTest {
	GraphDatabaseService graphDb;
	// 节点
	Node firstNode;
	Node secondNode;
	// 关系
	Relationship relationship;

	public static void main(String[] args) {
		Neo4jTest neo4jTest = new Neo4jTest();
		// neo4jTest.insertNode();
		neo4jTest.serchData();
	}

	/**
	 * 方法实现说明
	 * 
	 * @author ty
	 * @return
	 * @exception @date
	 *                2019/4/18 1:17
	 */
	void insertNode() {

		graphDb = DB.init();

		try (Transaction tx = DB.openTx()) {

			firstNode = graphDb.createNode();
			firstNode.setProperty("message666", "Hello666, ");
			firstNode.addLabel(MyLabels.Person);

			secondNode = graphDb.createNode();
			secondNode.setProperty("message23333", "World23333!");

			relationship = firstNode.createRelationshipTo(secondNode, RelTypes.KNOWS);
			relationship.setProperty("message888", "brave6666.....> Neo4j ");

			System.out.print(firstNode.getProperty("message666"));
			System.out.print(relationship.getProperty("message888"));
			System.out.print(secondNode.getProperty("message23333"));
			System.err.println();

			DB.commitTx(tx);

		}
		System.out.println();
		DB.registerShutdownHook(graphDb);
	}

	/**
	 * ** 方法实现说明
	 * 
	 * @author ty
	 * @return void
	 * @exception @date
	 *                2019年4月20日上午1:13:34
	 **
	 */
	void serchData() {
		graphDb = DB.init();
		try (Transaction tx = DB.openTx()) {
			// 查询带有Person标签的节点集合
			ResourceIterator<Node> persions = graphDb.findNodes(MyLabels.Person);

			// 查询带有Person标签 message属性为hello, 的节点集合
			// Map<String, Object> propertyValues = new HashMap<String, Object> ();
			// propertyValues.put("message", "hello,");
			// ResourceIterator<Node> persions=graphDb.findNodes(MyLabels.Person,
			// propertyValues);

			persions.forEachRemaining((entityTypeGraphNode) -> {
				System.out.println("Node id: " + entityTypeGraphNode.getId() + ",message property:"
						+ entityTypeGraphNode.getProperties("message666"));

				Iterable<Relationship> typeObjectsRelationships = entityTypeGraphNode.getRelationships();
				typeObjectsRelationships.forEach((relationship) -> {
					Node entityGraphNode = relationship.getEndNode();
					System.out.println("Real id: " + entityGraphNode.getId() + ",message property:"
							+ entityGraphNode.getProperty("message23333"));

				});
			});
			
			DB.commitTx(tx);
			DB.registerShutdownHook(graphDb);
		}
	}

	// 删除数据
	void removeData() {
		try (Transaction tx = graphDb.beginTx()) {

			firstNode.getSingleRelationship(RelTypes.KNOWS, Direction.OUTGOING).delete();
			firstNode.delete();
			secondNode.delete();
			// END SNIPPET: removingData
			DB.commitTx(tx);
		}
	}

}
