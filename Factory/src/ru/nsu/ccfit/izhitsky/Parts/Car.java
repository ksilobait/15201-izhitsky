package ru.nsu.ccfit.izhitsky.Parts;

public class Car
{
	private int idNumber;
	private Coachwork coachwork;
	private Engine engine;
	private Accessory accessory;

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
