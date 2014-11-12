package com.dhsit.pos;

public class Tables {

	long id;
	private String name = "";
    private boolean checked = false;

    public Tables()
    {
    }

    public Tables(String name)
    {
        this.name = name;
    }

    public Tables(String name, boolean checked)
    {
        this.name = name;
        this.checked = checked;
    }
    public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isChecked()
    {
        return checked;
    }

    public void setChecked(boolean checked)
    {
        this.checked = checked;
    }

    public String toString()
    {
        return name;
    }

    public void toggleChecked()
    {
        checked = !checked;
    }
}
