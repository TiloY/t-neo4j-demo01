package com.ty.neo4j.db;

import java.io.File;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class DB {
	// 文件的路径
	private static final String DB_PATH = "E:\\DevSoftWare\\hadoop\\neo4j-3.3.5\\data\\databases\\graph.db";
	// 创建file 指明我们想要操作的neo4j数据库的数据文件的路径
	private static final File databaseDir = new File(DB_PATH);
	// 全局只创建一个GraphDatabaseService实例
	private static GraphDatabaseService graphDb;

	public static GraphDatabaseService init() {
		graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(databaseDir);
		System.err.println("open neo4j " + graphDb);
		return graphDb;
	}

	public static void colse() {
		// 关闭数据库
		graphDb.shutdown();
		System.err.println("close neo4j " + graphDb);
	}
	
	public static void registerShutdownHook( final GraphDatabaseService graphDb )
    {
        // 用来确保数据库正确关闭的 一个回调方法
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                graphDb.shutdown();
                System.err.println("close neo4j " + graphDb);
            }
        } );
    }
	
	
	public static Transaction openTx() {
		return graphDb.beginTx();
	}
	
	public static void commitTx(Transaction tx) {
		tx.success();;
	}
	
	
//	public static void main(String[] args) {
//		DB.init();
//		DB.registerShutdownHook(graphDb);
//	}
}
