package ru.nsu.ccfit.izhitsky.factory.Parts;

public class Car
{
	private Coachwork coachwork;
	private Engine engine;
	private Accessory accessory;
	private int idNumber;

	public Car(int id_, Coachwork coachwork_, Engine engine_, Accessory accessory_)
	{
		this.idNumber = id_;
		this.coachwork = coachwork_;
		this.engine = engine_;
		this.accessory = accessory_;
	}

	public int getIdNumber()
	{
		return idNumber;
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
