package test;


public class StatLib {

	

	// simple average
	public static float avg(float[] x){
		float avg = 0;
		for (int i = 0; i < x.length; ++i)
			avg+= x[i];
		return avg/x.length;
	}

	// returns the variance of X and Y
	public static float var(float[] x){
		float variance = 0;
		float avg = avg(x);
		for (int i = 0; i < x.length; ++i)
			variance += (x[i] - avg)*(x[i] - avg);
		return variance / x.length;
	}

	// returns the covariance of X and Y
	public static float cov(float[] x, float[] y){
		float avgX = avg(x);
		float avgY = avg(y);
		float cov = 0 ;

		for (int i = 0; i < x.length; ++i)
			cov += (x[i] - avgX) * (y[i] - avgY);

		return cov/ (x.length) ;
	}


	// returns the Pearson correlation coefficient of X and Y
	public static float pearson(float[] x, float[] y){
		float cov = cov(x,y);
		double root = (Math.sqrt(var(x)) * Math.sqrt(var(y)));

		return (float) (cov/root);
	}

	// performs a linear regression and returns the line equation
	public static Line linear_reg(Point[] points){
		float[] x = new float[points.length];
		float[] y = new float[points.length];

		for (int i = 0; i < points.length;i++)
		{
			x[i] = points[i].x;
			y[i] = points[i].y;
		}

		float a = (cov(x,y) / var(x));
		float b = avg(y) - a*avg(x);

		return new Line(a,b);
	}

	// returns the deviation between point p and the line equation of the points
	public static float dev(Point p,Point[] points){
		return dev(p,linear_reg(points));
	}

	// returns the deviation between point p and the line
	public static float dev(Point p,Line l){
		return Math.abs(p.y - l.f(p.x));
	}
	
}
