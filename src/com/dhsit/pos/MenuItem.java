package com.dhsit.pos;

public class MenuItem {
	private long id;
	private String name="";
	private String category = "";
	private String subcategory = "";
	private String cost = "";
	private String image = "";
	private String nutri = "";
	private String info = "";
	private String likes = "";
	
	public MenuItem()
	{}
	
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
    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }
    public String getsubCategory()
    {
        return subcategory;
    }

    public void setsubCategory(String subcategory)
    {
        this.subcategory = subcategory;
    }
    public String getCost()
    {
        return cost;
    }

    public void setCost(String cost)
    {
        this.cost = cost;
    }
    
   public void setImage(String Image)
   {
	   this.image = image;
   }
   public String getImage()
   {
	   return image;
   }
   public void setNutri(String nutri)
   {
	   this.nutri = nutri;
   }
   public String getNutri()
   {
	   return nutri;
   }
   public void setInfo(String info)
   {
	   this.info =  info;
   }
   public String getInfo()
   {
	   return info;
   }



	

}
