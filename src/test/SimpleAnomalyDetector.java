package test;

import java.util.*;

public class SimpleAnomalyDetector implements TimeSeriesAnomalyDetector {
	private ArrayList<CorrelatedFeatures> _correlatedFeatures;

	//------------------------ Learn Methods ------------------------//

	@Override
	public void learnNormal(TimeSeries ts) {
		_correlatedFeatures = new ArrayList<>();
		String[] line1 = ts.getFeatures();
		float[][] data = ts.get_data();
		LinkedList<String> features = new LinkedList<>();
		Collections.addAll(features,line1);
		while(!features.isEmpty()){
			String f1 = features.pop();
			String f2 = correlatedWith(f1,ts);
			if(f2 == null){
				continue;
			}
			features.remove(f2);
			float correlation = StatLib.pearson(data[ts.getIndex(f1)],data[ts.getIndex(f2)]);
			Point[] points = new Point[data[0].length];
			for(int i =0;i< data[0].length;++i){
				points[i] = new Point(data[ts.getIndex(f1)][i],data[ts.getIndex(f2)][i]);
			}
			Line lin_reg = StatLib.linear_reg(points);
			float max_treshold = 0f;
			for(int i=0;i< points.length;++i){
				float treshold = StatLib.dev(points[i],lin_reg);
				if(treshold > max_treshold){
					max_treshold = treshold;
				}
			}
			float treshold = max_treshold * 1.1f;
			CorrelatedFeatures cf = new CorrelatedFeatures(f1,f2,correlation,lin_reg,treshold);
			_correlatedFeatures.add(cf);
		}
	}

	// get correlated feature of a given feature
	private String correlatedWith(String feature1,TimeSeries ts){
		String[] line1 = ts.getFeatures();
		int numF = ts.get_numOfFeatures();
		float[][] data = ts.get_data();
		int my_index= ts.getIndex(feature1);
		float[] pearson = new float[numF];
		for(int i =0;i<numF;++i){
			if (i == my_index){
				pearson[i]=0f;
				continue;
			}
			pearson[i] = StatLib.pearson(data[my_index],data[i]);
		}
		int max_index = 0;
		float max_pears = 0f;
		for(int i=0;i < numF;++i){
			if (pearson[i]>max_pears){
				max_index = i;
				max_pears = pearson[i];
			}
		}
		return line1[max_index];
	}



	//------------------------ Detect Methods ------------------------//
	@Override
	public List<AnomalyReport> detect(TimeSeries ts) {
		LinkedList<AnomalyReport> report = new LinkedList<>();
		float[][] data = ts.get_data();
		for(CorrelatedFeatures features : _correlatedFeatures){
			String f1 = features.feature1;
			String f2 = features.feature2;
			Point[] points = new Point[data[0].length];
			for(int i =0;i< data[0].length;++i){
				points[i] = new Point(data[ts.getIndex(features.feature1)][i],data[ts.getIndex(features.feature2)][i]);
			}
			for(int i = 0; i < ts.get_numOfLines() -1;++i){
				float dev = StatLib.dev(points[i],features.lin_reg);
				if(dev > features.threshold){
					report.add(new AnomalyReport(f1 + "-" + f2,i+1));
				}
			}
		}
		return report;
	}
	//------------------------ getNormal Methods ------------------------//
	public List<CorrelatedFeatures> getNormalModel(){
		
		return _correlatedFeatures;
	}
}
