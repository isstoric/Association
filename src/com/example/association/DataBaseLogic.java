package com.example.association;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataBaseLogic {
	
	private static final String DB_READONLY_NAME = "Association.sqlite";
	private SQLiteDatabase associationDatabase;	

	private static final String TABLE_NAME_READONLY = "Association";
	private static final String ID_ASSOCIATION = "_id";
	private static final String ASSOCIATION_PICTURES = "pictures";
	
	
	private static final String DB_READWRITE_NAME = "StateOfGame.sqlite";
	private SQLiteDatabase stateOfGameDatabase;

	private static final String TABLE_NAME_READWRITE_STATE = "Current_state";
	private static final String STATE_ID = "_id";
	private static final String STATE_NUMBER_OF_COUPLE = "number_of_couple";
	private static final String STATE_MAIN_PICTURES = "main_pictures";
	private static final String STATE_EXTRA_PICTURES = "extra_pictures";
	private static final String STATE_ID_ASSOCIATION = "id_association";
	
	private static final String TABLE_NAME_READWRITE_USED = "Used_pictures";
	private static final String USED_ID = "_id";
	private static final String USED_FIRST_COUPLE = "first_couple";
	private static final String USED_SECOND_PICTURES = "second_couple";
	private static final String USED_ID_ASSOCIATION = "id_association";
	private static final String USED_COUNT_PICTURES = "count_pictures";

	
	public DataBaseLogic(Context context) {
		DataBaseHelper dbFirstOpenHelper = 
				new DataBaseHelper(context, DB_READONLY_NAME, SQLiteDatabase.OPEN_READONLY);
		associationDatabase = dbFirstOpenHelper.getDb();
		DataBaseHelper dbSecondOpenHelper = 
				new DataBaseHelper(context, DB_READWRITE_NAME, SQLiteDatabase.OPEN_READWRITE);
		stateOfGameDatabase = dbSecondOpenHelper.getDb();
	}
	
	
    /**
     * ��������� ���������� ���� �� ����������� ������� � ���� ��� �� ����������
     * �������� ����������� ��������� ����.
     *
     * @return <ul>
     * <li><b>true</b>, ���� ���� ����������� �� �����-��
     * ������;
     * <li><b>false</b>, ������� ������ �������������� �
     * ����;</li></ul>
     */
    public boolean getCurrentState() {
        String selectQuery1 = "select * from Current_state";
	    Cursor cursor = stateOfGameDatabase.rawQuery(selectQuery1, null);
		if (cursor.getCount() == 0) {
			cursor.close();
			return false;
		} else {
			cursor.close();
			return true;
		}
    }
   
    /**
     * �������� ���� ��������-����������.
     * @param numberOfCouple ����� ����;
     * @param currentState <ul>
     * <li><b>true</b>, ���� ���� ����������� ��
     * �����-�� ������;</li>
     *  <li><b>false</b>, ������� ����������� �
     * ����;</li>
     * </ul>
     * @return ���� ��������-���������� � ���� ArrayList. 
     * ���� <b>������������ �������� - null</b>, �� � ���������� ��������� ��������
     * ��������� ����, ��������� � ������������� ������� � �����, ���� � ������������� 
     * �������� �����������.
     */
    public ArrayList<String> getMainPictures(boolean currentState, int numberOfCouple) {
    	
        ArrayList<String> associationPictures = new ArrayList<String>();
		String randomAssociation;
		int idRandomAssociation;
		
        if (currentState) {
        	//���� ��� ��������� � currentState
        	String query = "select * from Current_state where number_of_couple = " + numberOfCouple;
    		Cursor cursor = stateOfGameDatabase.rawQuery(query, null);
    		if (cursor.getCount() == 0) {
    			cursor.close();
    			return null;
    		}
    		cursor.moveToFirst();
    		do {
    			associationPictures.add(cursor.getString(cursor.getColumnIndex(STATE_MAIN_PICTURES)));
    		} while (cursor.moveToNext());
    		associationPictures = getFileNamesFromRows(associationPictures);
    		cursor.close();
        } else {
        	//���� ���� �������� �� usedPictures
        	String query1 = "select id_association from Used_pictures where count_pictures = 4";
    		Cursor cursor = stateOfGameDatabase.rawQuery(query1, null);
    		String findNotUsedAssociation;
    		if (cursor.getCount() != 0) {
    			//���� ���� �� ��������� �������������� ����������
    			ArrayList<Integer> idAssociation = new ArrayList<Integer>();
        		cursor.moveToFirst();
        		do {
        			idAssociation.add(cursor.getInt(cursor.getColumnIndex(STATE_ID_ASSOCIATION)));
        		} while (cursor.moveToNext());
        		cursor.close();
        		findNotUsedAssociation = "select * from Association where _id not in " + 
        				idAssociation.toString().replace("[", "(").replace("]", ")");
    		} else {
				//���� ��� �� ��������� �������������� ����������
				findNotUsedAssociation = "select * from Association";
        		cursor.close();
			}
    		//���� ���� ��������� �� �� ����� (��� ������) �������������� ����������
			Cursor cursorFindNotUsedAssociation = associationDatabase.rawQuery(findNotUsedAssociation, null);
    		if (cursorFindNotUsedAssociation.getCount() == 0) {
    			cursorFindNotUsedAssociation.close();
    			return null;
    		} else {
    			idRandomAssociation = (int) (Math.random() * cursorFindNotUsedAssociation.getCount());
    			cursorFindNotUsedAssociation.moveToFirst();
        		cursorFindNotUsedAssociation.moveToPosition(idRandomAssociation);
        		idRandomAssociation++;//����� �� �������� ������
        		randomAssociation = cursorFindNotUsedAssociation.getString(cursorFindNotUsedAssociation.getColumnIndex(ASSOCIATION_PICTURES));
       		 	Log.d("MainActivity","idRandomAssociation"+idRandomAssociation);
       		 	Log.d("MainActivity","randomAssociation"+randomAssociation);
        		cursorFindNotUsedAssociation.close();
    		}
            //�������, ������������ �� �� ������ ��� ����������
            String usedOrNotUsed = "select first_couple from Used_pictures where "
                    + "id_association = " + idRandomAssociation;
			Cursor cursorUsedOrNotUsed = stateOfGameDatabase.rawQuery(usedOrNotUsed, null);
            ArrayList<String> listRandomPictures = getFileNamesFromRow(randomAssociation);
    		if (cursorUsedOrNotUsed.getCount() == 0) {
    			cursorUsedOrNotUsed.close();
    			//������ ��� ���������� ������ �� ������������
    			for(int i = 0; i<2; i++){
    				listRandomPictures.remove((int) (Math.random() * listRandomPictures.size()));
    			}
    			//�������� �������� ����
    			associationPictures = listRandomPictures;
    			String insertCurrentState = "insert into Current_state " +
    					"('number_of_couple', 'main_pictures', 'id_association') values" +
    					" ("+numberOfCouple+", '"+getStringOfFileNamesFromList(associationPictures)+"', "+idRandomAssociation+")";
    		    stateOfGameDatabase.execSQL(insertCurrentState);
    			String insertUsedPictures = "insert into Used_pictures " +
    					"('first_couple', 'id_association', 'count_pictures') values" +
    					" ('"+getStringOfFileNamesFromList(associationPictures)+"', "+idRandomAssociation+", 2)";
    		    stateOfGameDatabase.execSQL(insertUsedPictures);    			
    		} else {
    			cursorUsedOrNotUsed.close();
    			//���������� ��� ���� ������������
    			String usedAssociation = "select first_couple from Used_pictures where id_association = "
    					+ idRandomAssociation;
    			Cursor cursorUsedAssociation = stateOfGameDatabase.rawQuery(usedAssociation, null);
        		if (cursorUsedAssociation.getCount() != 1) {
        			cursorUsedAssociation.close();
        			return null;
        		} else {
        			ArrayList<String> listUsedPictures;
        			cursorUsedAssociation.moveToFirst();
        			String temp = cursorUsedAssociation.getString(cursorUsedAssociation.getColumnIndex(USED_FIRST_COUPLE));
        			listUsedPictures = getFileNamesFromRow(temp);
        			Log.d("MainActivity","listUsedPictures"+listUsedPictures);
        	        Log.d("MainActivity","listRandomPictures"+listRandomPictures);
        	        Log.d("MainActivity","id"+idRandomAssociation);
        			cursorUsedAssociation.close();
        			//��������� ������ ���������������� ��������
        			for(String i: listUsedPictures){
        				if(listRandomPictures.contains(i)){
        					listRandomPictures.remove(i);
        				}
        			}
        			//�������� �������� ����
        			associationPictures = listRandomPictures;
        		}
    			String insertCurrentState = "insert into Current_state " +
    					"('number_of_couple', 'main_pictures', 'id_association') values" +
    					" ("+numberOfCouple+", '"+getStringOfFileNamesFromList(associationPictures)+"', "+idRandomAssociation+")";
    		    stateOfGameDatabase.execSQL(insertCurrentState);
    			String updateUsedPictures = "update Used_pictures set " +
    					"'second_couple' = '"+getStringOfFileNamesFromList(associationPictures)+"', 'count_pictures' = 4 " +
    							"where id_association = " + idRandomAssociation;
    		    stateOfGameDatabase.execSQL(updateUsedPictures);
    		}    		
        }
        return associationPictures;
    }
    
    public ArrayList<ArrayList<String>> qwe() {
        ArrayList<String> first = new ArrayList<String>();
        ArrayList<String> second = new ArrayList<String>();
        ArrayList<String> third = new ArrayList<String>();
        ArrayList<String> fourth = new ArrayList<String>();
        ArrayList<ArrayList<String>> count = new ArrayList<ArrayList<String>>();
           String query = "select * from Used_pictures";
           Cursor cursor = stateOfGameDatabase.rawQuery(query, null);
     cursor.moveToFirst();
     do {
      first.add(cursor.getString(cursor.getColumnIndex(USED_FIRST_COUPLE)));
      second.add(cursor.getString(cursor.getColumnIndex(USED_SECOND_PICTURES)));
      third.add(cursor.getString(cursor.getColumnIndex(USED_ID_ASSOCIATION)));
      fourth.add(cursor.getString(cursor.getColumnIndex(USED_COUNT_PICTURES)));
     } while (cursor.moveToNext());
     count.add(new ArrayList<String>());
     count.add(new ArrayList<String>());
     count.add(new ArrayList<String>());
     count.add(new ArrayList<String>());
     count.add(first);
     count.add(second);
     count.add(third);
     count.add(fourth);
           return count;
       }

    /**
     * �������� ���� �� ������ �������� ��� ����� ������.
     *
     * @param currentState <ul>
     * <li><b>true</b>, ���� ���� ����������� ��
     * �����-�� ������;</li> 
     * <li><b>false</b>, ������� ����������� �
     * ����;</li></ul>
     * @param countExtraPictures ������ ���������� ������ �������� ��� ������
     * ���� ����������;
     * @return ����� ������ �������� � ���� ArrayList.
     * ���� <b>������������ �������� - null</b>, �� � ���������� ��������� ��������
     * ��������� ����, ��������� � ������������� ������� � �����, ���� � ������������� 
     * �������� �����������.
     */
    public ArrayList<String> getExtraPictures(boolean currentState, int countExtraPictures) {
        ArrayList<String> extraPictures = new ArrayList<String>();
        if (currentState) {
            //����� ����� �� �������� ���������
            String query = "select extra_pictures from Current_state";
    		Cursor cursor = stateOfGameDatabase.rawQuery(query, null);
    		if (cursor.getCount() == 0) {
    			cursor.close();
    			return null;
    		} else {
    			cursor.moveToFirst();
	    		do {
	    			extraPictures.add(cursor.getString(cursor.getColumnIndex(STATE_EXTRA_PICTURES)));
	    		} while (cursor.moveToNext());
	    		//��������� �� ������������� ������ �������
	    		if(!extraPictures.contains(null)){
	    			extraPictures = getFileNamesFromRows(extraPictures);
	    		} else {
	    			return null;
	    		}
	    		cursor.close();
    		}
        } else {
            //��������� ����� ����� ������ ��������
        	String query1 = "select id_association from Current_state";
    		Cursor cursor = stateOfGameDatabase.rawQuery(query1, null);
    		if (cursor.getCount() == 0) {
    			cursor.close();
    			return null;
    		} else {
    			ArrayList<Integer> idAssociation = new ArrayList<Integer>();
        		cursor.moveToFirst();
        		do {
        			idAssociation.add(cursor.getInt(cursor.getColumnIndex(STATE_ID_ASSOCIATION)));
        		} while (cursor.moveToNext());
        		cursor.close();
        		String findExtraPictures = "select * from Association where _id not in " + 
        				idAssociation.toString().replace("[", "(").replace("]", ")");
        		Cursor cursorFindExtraPictures = associationDatabase.rawQuery(findExtraPictures, null);
        		ArrayList<String> potentialExtraPictures = new ArrayList<String>();
        		cursorFindExtraPictures.moveToFirst();
        		do {
        			potentialExtraPictures.add(cursorFindExtraPictures.
        					getString(cursorFindExtraPictures.getColumnIndex(ASSOCIATION_PICTURES)));
        		} while (cursorFindExtraPictures.moveToNext());
        		cursorFindExtraPictures.close();
        		potentialExtraPictures = getFileNamesFromRows(potentialExtraPictures);
        		//�������� 4*countExtraPictures ��������� ������ ��������
        		for(int i=0; i<countExtraPictures*4; i++){
        			int random = (int) (Math.random() * potentialExtraPictures.size());
        			extraPictures.add(potentialExtraPictures.get(random));
        			potentialExtraPictures.remove(random);
        		}
			}
            //���������� � ���� Current_state
            int count = 0;
            ArrayList<String> insert = new ArrayList<String>();
            ArrayList<String> temp = new ArrayList<String>();
            for (String i : extraPictures) {
                temp.add(i);
                count++;
                if (count == countExtraPictures) {
                    insert.add(getStringOfFileNamesFromList(temp));
                    temp.clear();
                    count = 0;
                }
            }
            int numberOfCouple = 1;
            for(String setPictures: insert){
    			String updateCurrentPictures = "update Current_state set " +
    					"'extra_pictures' = '"+setPictures+"' where number_of_couple = " + numberOfCouple;
    		    stateOfGameDatabase.execSQL(updateCurrentPictures);
    		    numberOfCouple++;
            }
        }
        return extraPictures;
    }

    /**
     * ������� ���� �������� ����. ������������ ��� �������� ����� ����.
     *
     * @return <ul>
     * <li><b>true</b>, �������� ���� ������ �������;</li></ul>
     */
    public boolean clearGame() {
        String query1 = "delete from Current_state";
        String query2 = "delete from Used_pictures";
	    stateOfGameDatabase.execSQL(query1);
	    stateOfGameDatabase.execSQL(query2);
        return true;
    }

    /**
     * ������� ������� ��������� ������. ������������ ����� ���������
     * ����������� ������.
     *
     * @return <ul>
     * <li><b>true</b>, ��������� ������ ������� �������;</li></ul>
     */
    public boolean clearCurrentState() {
        String query = "delete from Current_state";
	    stateOfGameDatabase.execSQL(query);
        return true;
    }

    /**
     * ���������� ���������� �������� � ���� ������.
     *
     * @return ����� ���������� �������� � ���� ������
     */
    public int countAllPictures() {
        int count = 0;
        String query = "select * from Association";
//        Cursor cursor = databaseRead.rawQuery(query, null);
        //�������� ����� �����
        //������� ���������� �����
        //�������� �� 4
        return count;
    }

    /**
     * ����� ������� ���������, ���������� �� �������� ��� ������������ ������
     * ������. ���� �������� ������������, �� ���������� ����� ��� ��������
     * ��������� ����. <br><b>������ ���������� ����� ����������� ����������
     * ������.</b>
     */
    public void checkPictures(int countAllPictures) {
        int count = 0;
        String query = "select * from usedPictures";
        //            Cursor cursor = databaseRead.rawQuery(selectQuery, null);
        //�������� ����� �����
        //������� ���������� �����
        count = count * 2 + 8;
        if (count > countAllPictures) {
            clearGame();
        }
    }
    
    private String getStringOfFileNamesFromList(ArrayList<String> rows) {
        String fileNames = "";
        for (String el : rows) {
            String[] temp;
            temp = el.replaceAll(" ", "").split(";");
            int count = 0;
            for (int i = 0; i < temp.length; i++) {
                fileNames = fileNames + temp[count] + "; ";
                count++;
            }
        }
        return fileNames;
    }

    private ArrayList<String> getFileNamesFromRows(ArrayList<String> rows) {
        ArrayList<String> fileNames = new ArrayList<String>();
        for (String el : rows) {
            String[] temp;
            temp = el.replaceAll(" ", "").split(";");
            int count = 0;
            for (int i = 0; i < temp.length; i++) {
                fileNames.add(temp[count]);
                count++;
            }
        }
        return fileNames;
    }
    
    private ArrayList<String> getFileNamesFromRow(String row) {
        ArrayList<String> fileNames = new ArrayList<String>();
            String[] temp;
            temp = row.replaceAll(" ", "").split(";");
            int count = 0;
            for (int i = 0; i < temp.length; i++) {
                fileNames.add(temp[count]);
                count++;
            }
        return fileNames;
    }
}
