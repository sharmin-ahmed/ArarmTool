package SeparationOfDuty;

import java.util.*;

public class OrcaCluster {

	ArrayList<String> members;
	ArrayList<String> permissions;
	ArrayList<String> children;
	ArrayList<String> parents;
    String clusterName;
    ArrayList<OrcaCluster> childClusters;
    ArrayList<OrcaCluster> parentClusters;


    public OrcaCluster(String clusterName) {

    	members = new ArrayList<String>();
		permissions = new ArrayList<String>();
		children = new ArrayList<String>();
		parents = new ArrayList<String>();
		childClusters = new ArrayList<OrcaCluster>();
		parentClusters = new ArrayList<OrcaCluster>();
		this.clusterName = clusterName;
    }

    public OrcaCluster() {

    	this("");
    }

    public void addMember(String memberName){

    	if(!isMemberPresent(memberName)){
    		members.add(memberName);
    	}
    }

    public void addPermission(String permissionName){

    	if(!isPermissionPresent(permissionName)){
    		permissions.add(permissionName);
    	}
    }

    public void removeChildCluster(OrcaCluster child){
    	childClusters.remove(childClusters.indexOf(child));
    	children.remove(children.indexOf(child.getName()));
    }

    public void removeParentCluster(OrcaCluster parent){
    	parentClusters.remove(parentClusters.indexOf(parent));
    	parents.remove(parents.indexOf(parent.getName()));
    }

    public void addChildCluster(OrcaCluster child){
    	if(!isChildPresent(child.getName())){
    		childClusters.add(child);
    		addChildren(child.getName());
    		
    		for(int i=0; i<child.permissionCount(); i++){
    			this.removePermission(child.permissions.get(i));
    		}
    	}
    }

    public void addChildren(String childName){

    	if(!isChildPresent(childName)){
    		children.add(childName);
    	}
    }

    public void addParentCluster(OrcaCluster parent){
    	if(!isParentPresent(parent.getName())){
    		parentClusters.add(parent);
    		addParent(parent.getName());
    	}
    }

    public void addParent(String parentName){

    	if(!isParentPresent(parentName)){
    		parents.add(parentName);
    	}
    }

    public void setName(String clusterName){
    	this.clusterName = clusterName;
    }

    public String getName(){
    	return clusterName;
    }


    public int memberCount(){
    	return members.size();
    }

    public int permissionCount(){
    	return permissions.size();
    }

    public int childCount(){
    	return children.size();
    }

    public boolean isMemberPresent(String memberName){
    	return members.contains(memberName);
    }

    public boolean isPermissionPresent(String permissionName){
    	return permissions.contains(permissionName);
    }

    public boolean isChildPresent(String childName){
    	return children.contains(childName);
    }

    public boolean isParentPresent(String parentName){
    	return parents.contains(parentName);
    }

    public void removePermission(String permissionName){
    	permissions.remove(permissions.indexOf(permissionName));
    }

}