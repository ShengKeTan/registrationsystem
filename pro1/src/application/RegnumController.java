package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


public class RegnumController implements Initializable{
	
	static String sregnum = null;
	static String state = null;
	static String fee = null;
	
	//quit reg
	private ObservableList<Quitreg> quitdate = FXCollections.observableArrayList();
	@FXML private TableView<Quitreg> reglist;
	@FXML private TableColumn<Quitreg, String> regnum, depname, docname, time, type, quitreg, regfee;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		regnum.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getRegnum()));
		depname.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getDepname()));
		docname.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getDocname()));
		time.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getTime()));
		type.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getType()));
		quitreg.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getQuitreg()));
		regfee.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getRegfee()));
		//监听列表选中事件
		reglist.getSelectionModel().selectedItemProperty().addListener(// 选中某一行 
				new ChangeListener<Quitreg>() { 
					@Override 
					public void changed( ObservableValue<? extends Quitreg> observableValue, 
							Quitreg oldItem, Quitreg newItem) { 
						sregnum = newItem.getRegnum();
						state = newItem.getQuitreg();
						System.out.println(sregnum + state); 
						}
				});
	}
	@FXML
	private void show_regdate() {
		quitdate.clear();
		//connect to mysql
		ContoMysql con = new ContoMysql();
		Connection mycon = con.connect2mysql();
		//get info
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "SELECT register.reg_id, department.name, doctor.name, register.reg_datetime, register_category.speciallist,"
					+ " register.unreg, register.reg_fee"
					+ " FROM register, department, doctor, register_category, patient"
					+ " WHERE register.pid=patient.pid"
					+ " AND register.docid=doctor.docid"
					+ " AND register.catid=register_category.catid"
					+ " AND register_category.depid=department.depid"
					+ " AND patient.pid='%1$s'"
					+ " ORDER BY register.reg_id";
			sql = String.format(sql, LoginController.ID);
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
			System.out.println(sql);
			//pStatement.setString(1, LoginController.ID);
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			rs = pStatement.executeQuery();
			String pregnum = null;
			String pdepname = null;
			String pdocname = null;
			String ptime = null;
			String ptype = null;
			String pquitreg = null;
			String pregfee = null;
			while(rs.next()) {
				pregnum = rs.getString("register.reg_id");
				pdepname = rs.getString("department.name");
				pdocname = rs.getString("doctor.name");
				ptime = rs.getString("register.reg_datetime");
				pregfee = rs.getString("register.reg_fee");
				if(rs.getInt("register_category.speciallist") == 1) {
					ptype = "专家号";
				}
				else ptype = "普通号";
				if(rs.getString("register.unreg").equals("1")) {
					pquitreg = "可退";
				}
				else pquitreg = "不可退";
				quitdate.add(new Quitreg(pregnum,pdepname,pdocname,ptime,ptype,pquitreg,pregfee));
			}
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			mycon.close();
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		reglist.setItems(quitdate);
	}
	@FXML
	private void on_getback_click() {
		//connect to mysql
		ContoMysql con = new ContoMysql();
		Connection mycon = con.connect2mysql();
		//get info
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		String sql = null;
		//update unreg
		try {
			sql = "UPDATE register SET register.unreg = '%1$s' WHERE reg_id = '%2$s'";
			String up = String.format(sql, "0", sregnum);
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
			System.out.println(up);
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			pStatement.executeUpdate();
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		//get back money
		try {
			sql = "UPDATE patient SET balance = '%1$s' WHERE pid = '%2$s'";
			String up = String.format(sql, "0", LoginController.ID);
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	@FXML
	public void on_back_click() {
		Main.setPatUI();
	}

}
