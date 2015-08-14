package SeparationOfDuty; 
import java.util.*;
import java.sql.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class OsbornReidWessonAlgorithm extends JFrame{

 ArrayList<OrcaCluster> clusters;
 String userTable;
	String permissionTable;
	String permissionConstraintTable;
	String permissionAssignmentTable;

 public OsbornReidWessonAlgorithm(String userTable, String permissionTable, String permissionConstraintTable, String permissionAssignmentTable){
	 
	 	this.userTable = userTable;
		this.permissionTable = permissionTable;
		this.permissionConstraintTable = permissionConstraintTable;
		this.permissionAssignmentTable = permissionAssignmentTable;

  clusters = new ArrayList<OrcaCluster>();
 }

 public void addBasicCluster(OrcaCluster cluster){
     clusters.add(cluster);
    }

    public void addBasicCluster(String member, String permission){
     OrcaCluster cluster = new OrcaCluster();
     cluster.addMember(member);
     cluster.addMember(permission);
     addBasicCluster(cluster);
    }


    public void algorithm(){

     ArrayList<OrcaCluster> tempClusters = new ArrayList<OrcaCluster>();
	 int roles=0;

     while(!clusters.isEmpty()){

      OrcaCluster currentCluster = clusters.remove(0);
      boolean matched = false;

      for(int i=0; i<clusters.size(); i++){

       if(isSubsetCluster(currentCluster, clusters.get(i)) && isSubsetCluster(clusters.get(i), currentCluster)){

        for(int j=0; j<currentCluster.members.size(); j++){
         clusters.get(i).addMember(currentCluster.members.get(j));
        }

        matched = true;

       }
      }

      if(!matched){
       tempClusters.add(currentCluster);
      }
     }

     clusters = tempClusters;


     for(int i=0; i<clusters.size(); i++){

      for(int j=0; j<clusters.size(); j++){

       if(i != j){

        if(isSubsetCluster(clusters.get(i), clusters.get(j))){
         clusters.get(i).addChildren(clusters.get(j).getName());
        }
       }
      }
     }


    }


    public boolean isSubsetCluster(OrcaCluster cluster1, OrcaCluster cluster2){

     for(int i=0;i<cluster2.permissions.size();i++){
      if(!cluster1.isPermissionPresent(cluster2.permissions.get(i))){

       return false;

      }

     }
     return true;

    }


    public void draw(){


 DbMySQL dbMySQL = new DbMySQL();

 //ResultSet rset=dbMySQL.selectQuery("SELECT DISTINCT grantee FROM noise_test_roles ORDER BY grantee;");
 
 ResultSet rset=dbMySQL.selectQuery("SELECT DISTINCT user_id FROM "+this.permissionAssignmentTable+" ORDER BY user_id;");
 //ResultSet rset=dbMySQL.selectQuery("SELECT DISTINCT user_id FROM noise_test_user_permission_assignment ORDER BY user_id;");
 int index =0;

 try{
   while(rset.next()){
       OrcaCluster cluster = new OrcaCluster(""+index++);
       String user=rset.getString ("user_id");
       cluster.addMember(user);

    //ResultSet rsetUser=dbMySQL.selectQuery("SELECT DISTINCT table_name, privilege_type FROM noise_test_roles WHERE grantee=\""+user+"\";");

    ResultSet rsetUser=dbMySQL.selectQuery("SELECT DISTINCT table_name, permission_id FROM "+this.permissionAssignmentTable+" WHERE user_id=\""+user+"\";");
    //ResultSet rsetUser=dbMySQL.selectQuery("SELECT DISTINCT table_name, permission_id FROM noise_test_user_permission_assignment WHERE user_id=\""+user+"\";");
    while(rsetUser.next()){

     String table=rsetUser.getString ("table_name");
     String privilege=rsetUser.getString ("permission_id");
     cluster.addPermission(""+table+"-"+privilege);


    }

       addBasicCluster(cluster);
      }
  }
    catch(Exception ex){
    System.out.println("Exception '"+ex.toString()+"' in dbMySQL");
    }
    finally{
     dbMySQL.closeResultset();
     dbMySQL.closeStatement();
    }


     algorithm();
     
     String role = null;
     String member = null;
     String permission = null;
     String child = null;
     try{
         
    	 for(int i=0; i<clusters.size(); i++){
    		 role = clusters.get(i).getName();
    		 for(int j=0; j<clusters.get(i).members.size(); j++){
    			 member = clusters.get(i).members.get(j);
    			 dbMySQL.updateQuery("insert into `test_user_role_assignment` values (\"R"+role+"\",\""+member+"\");");
    		 }
    		 for(int k=0; k<clusters.get(i).permissions.size(); k++){
    			 permission = clusters.get(i).permissions.get(k);
    			 dbMySQL.updateQuery("insert into `test_role_permission_assignment` values (\"R"+role+"\",\""+permission+"\");");
    	     }
    		 for(int x=0; x<clusters.get(i).children.size(); x++){
 	    		child = clusters.get(i).children.get(x);
 	    		dbMySQL.updateQuery("insert into `test_role_child_assignment` values (\"R"+role+"\",\"R"+child+"\");");
 	    	}
    	 }	 
    	 
     }
     
     catch(Exception ex){
    	 System.out.println("Exception '"+ex.toString()+"' in dbMySQL");
     }
     finally{
    	 dbMySQL.closeResultset();
         dbMySQL.closeStatement();
     }

	
  
     for(int i=0; i<clusters.size(); i++){

     // Object v = graph.insertVertex(parent, null, ""+i, i*100, 5000-i*100, 80, 30);
      //vertices.put(clusters.get(i).getName(), v);

      System.out.println("Role: "+clusters.get(i).getName());
      

      for(int j=0; j<clusters.get(i).members.size(); j++){
       System.out.println("Member "+j+":"+clusters.get(i).members.get(j));
      }

      for(int k=0; k<clusters.get(i).permissions.size(); k++){
       System.out.println("Permission "+k+":"+clusters.get(i).permissions.get(k));
      }

      for(int x=0; x<clusters.get(i).children.size(); x++){
      System.out.println("Child "+x+":"+clusters.get(i).children.get(x));
      }

      System.out.println("---------------");
     }

 
     RoleGraph roleGraph = new RoleGraph();

  roleGraph.addRoles(clusters);

  	System.out.println("# of roles "+roleGraph.roleCount());

  	JOptionPane.showMessageDialog( null, "# of roles "+roleGraph.roleCount(), "Role Count", JOptionPane.PLAIN_MESSAGE );
  	
  	System.out.println("# of user to role Edges "+roleGraph.memberToRoleCount());
	System.out.println("# of role to permission Edges "+roleGraph.roleToPermissionCount());
	System.out.println("The optimization metric value is: " +roleGraph.zhangOptimisationMetric());

/*


     for(int i=0; i<clusters.size(); i++){

      for(int j=0; j<clusters.get(i).children.size(); j++){
       graph.insertEdge(parent, null, "", vertices.get(clusters.get(i).getName()), vertices.get(clusters.get(i).children.get(j)));
      }
     }

  graph.getModel().endUpdate();

  mxGraphComponent graphComponent = new mxGraphComponent(graph);
  getContentPane().add(graphComponent);
*/
    }

     public static void main(String args[]){

     //OsbornReidWessonAlgorithm alg = new OsbornReidWessonAlgorithm();
     //alg.draw();

    }


}