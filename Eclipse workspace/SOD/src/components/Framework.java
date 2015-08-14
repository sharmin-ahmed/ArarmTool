
package components;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.Toolkit;

import javax.swing.*;

import SeparationOfDuty.DbMySQL;

import java.util.*;
import java.sql.*;
import java.awt.Window.Type;

public class Framework extends JFrame implements ActionListener
{	
	
	DbMySQL dbMySQL;
	JComboBox userTableSelector;
	JComboBox permissionTableSelector;
	
	public  Framework(){
		dbMySQL = new DbMySQL();
		Toolkit tk = Toolkit.getDefaultToolkit();  
		int xSize = ((int) tk.getScreenSize().getWidth());  
		int ySize = ((int) tk.getScreenSize().getHeight());  
		this.setSize(xSize,ySize);		
		//Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		//this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		setTitle("Framework");
		getContentPane().setLayout(null);
		
		JLabel lblSelectTheUsers = new JLabel("Select the user table");
		lblSelectTheUsers.setBounds(300, 300, 321, 27);
		getContentPane().add(lblSelectTheUsers);
		
		userTableSelector = new JComboBox();	
		userTableSelector.setBounds(621, 300, 350, 27);
		getContentPane().add(userTableSelector);
		
		JLabel label = new JLabel("Select the permission table");
		label.setBounds(300, 350, 321, 27);
		getContentPane().add(label);
		
		permissionTableSelector = new JComboBox();
		permissionTableSelector.setBounds(621, 350, 350, 27);
		getContentPane().add(permissionTableSelector);
		
		DbMySQL dbMySQL = new DbMySQL();
		
		ResultSet rset=dbMySQL.selectQuery("SHOW TABLES FROM cic;");

		  try{
		   while(rset.next()){
		       String tables = rset.getString ("Tables_in_cic");
	
		       //String privilege_type=rset.getString ("PRIVILEGE_TYPE");

		       //int id = rset.getInt("id");
		       //String name = rset.getString("name");

		       userTableSelector.addItem(tables);
		       permissionTableSelector.addItem(tables);
		      }
		  }
		    catch(Exception ex){
		    System.out.println("Exception '"+ex.toString()+"' in dbMySQL.getInactiveURLs()");
		    }
		    finally{
		     dbMySQL.closeResultset();
		     dbMySQL.closeStatement();
		    }
		  
		
		JButton btnSaveContinue = new JButton("Continue");
		btnSaveContinue.setBounds(450, 450, 380, 27);
		btnSaveContinue.addActionListener(this);
		getContentPane().setLayout(null);
		btnSaveContinue.setActionCommand("Open");
		getContentPane().add(btnSaveContinue);
	}
	public void actionPerformed(ActionEvent e)
    {
		String userTable = this.userTableSelector.getSelectedItem().toString();
		String permissionTable = this.permissionTableSelector.getSelectedItem().toString();
		
        String cmd = e.getActionCommand();

        if(cmd.equals("Open"))
        {
            dispose();
            UPConstraintJFrame frame = new UPConstraintJFrame();
            frame.setUserTable(userTable);
            frame.setPermissionTable(permissionTable);
        }
    }
	
	public static void main(String[] args)
    {
		
		/*		SOD sod=new SOD();
		double[][] user_similarity = sod.user_similarity();
		
		for (int i =0; i<user_similarity.length; i++){
			for(int j=0; j<user_similarity.length; j++){
				System.out.println("Similarity between user="+i+" and user="+j+" is "+user_similarity[i][j]);
			}
		}
		*/
		SwingUtilities.invokeLater(new Runnable(){

            @Override
            public void run()
            {
                new Framework().setVisible(true);
            }

        });
    }
}