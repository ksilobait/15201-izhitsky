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
}
