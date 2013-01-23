package br.ufrj.jfirn.intelligent;

import br.ufrj.jfirn.common.Point;

public interface MobileObstacleStatistics {

	public int samplesCount();

	public int getObstacleId();

	public Point lastKnownPosition();

	public double xMean();
	public double xVariance();

	public double yMean();
	public double yVariance();

	public double speedMean();
	public double speedVariance();

	public double directionMean();
	public double directionVariance();

	public double xyCorrelation();

	public double xyCovariance();

	public double speedDirectionCorrelation();

	public double speedDirectionCovariance();

}
