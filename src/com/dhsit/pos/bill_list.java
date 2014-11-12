package com.dhsit.pos;

public class bill_list {
	public String order_no;
	public String price;
	public String name;
	public String quantity;
	public String table_no;
	public String waiter;
	public String toString()
	{
		return order_no+price+name+quantity+table_no;
	}

}
