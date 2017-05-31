package ru.nsu.ccfit.izhitsky.Parts;

public class Car
{
	private Coachwork coachwork;
	private Engine engine;
	private Accessory accessory;

	public Car(int id_, Coachwork coachwork_, Engine engine_, Accessory accessory_)
	{
		this.coachwork = coachwork_;
		this.engine = engine_;
		this.accessory = accessory_;
	}

	public Coachwork getCoachwork()
	{
		return coachwork;
	}

	public Engine getEngine()
	{
		return engine;
	}

	public Accessory getAccessory()
	{
		return accessory;
	}
}
