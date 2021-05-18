package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class TimeSeries {

	private LinkedList<String[]> _data;
	private int _numOfFeatures;
	private int _numOfLines;

	public int get_numOfFeatures() {
		return _numOfFeatures;
	}

	public int get_numOfLines() {
		return _numOfLines;
	}

	/// Ctor
	public TimeSeries(String csvFileName) {
		_data = new LinkedList<>();
		readCSV(csvFileName);
		_numOfFeatures = _data.getFirst().length;
		_numOfLines = _data.size();
	}

	// read csv title file
	private void readCSV(String f_name){
		if (f_name == null)
			return;

		String line;
		try {
			//parsing a CSV file into BufferedReader class constructor
			BufferedReader br = new BufferedReader(new FileReader(f_name));
			while ((line = br.readLine()) != null)   //returns a Boolean value
			{
				String[] sLine = line.split((","));
				//Float[] fLine = Arrays.stream(sLine).map(Float::valueOf).toArray(Float[]::new);
				_data.add(sLine);
			}
			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// get value by feature index and line
	public float getValue(int feature, int index) {
		String[] row = _data.get(index);
		return Float.parseFloat(row[feature]);
	}

	// get value by feature name and line
	public float getValue(String feature, int index) {
		return getValue(getIndex(feature),index);
	}

	// get Data
	public float[][] get_data() {
		float[][] data = new float[this.get_numOfFeatures()][this.get_numOfLines() -1];
		String[][] data_s = _data.toArray(new String[0][0]);
		for(int i =0; i < _numOfLines -1;++i){
			for(int j=0; j< _numOfFeatures; ++j){
				data[j][i] = Float.parseFloat(data_s[i+1][j]);
			}
		}
		return data;
	}


	// convert feature to index
	public int getIndex(String feature) {
		String[] first = _data.getFirst();
		for(int i = 0; i< first.length;++i){
			if (first[i].equals(feature))
				return i;
		}
		return -1;
	}

	// get features names
	public String[] getFeatures(){
		return _data.getFirst();
	}
}