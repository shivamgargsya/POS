package com.dhsit.pos;

public class listdata{
	public String order_no;
	public String date;
	public String table_no;
	public String customer_name;
	public String waiter_name;
	public String total_amount;
	public String payment_mode;
	public String get_order_no()
	{
		return this.order_no;
	}
	public String get_date()
	{
		return this.date;
	}
	public String get_table_no()
	{
		return this.table_no;
	}
	public String get_customer_name()
	{
		return this.customer_name;
	}
	public String get_waiter_name()
	{
		return this.waiter_name;
	}
	public String get_total_amount()
	{
		return this.total_amount;
	}
	public String get_payment_mode()
	{
		return this.payment_mode;
	}
	public void set_order_no(String order_no)
	{
		this.order_no=order_no;
	}
	public void set_date(String date)
	{
		this.date=date;
	}
	public void set_table_no(String table_no)
	{
		this.table_no=table_no;
	}
	public void set_customer_name(String customer_name)
	{
		this.customer_name=customer_name;
	}
	public void set_waiter_name(String waiter_name)
	{
		this.waiter_name=waiter_name;
	}
	public void set_total_amount(String total_amount)
	{
		this.total_amount=total_amount;
	}
	public void set_payment_mode(String payment_mode)
	{
		this.payment_mode=payment_mode;
	}
	public String toString()
	{
		String s=this.customer_name+this.date+"\n"+this.order_no+this.payment_mode+this.table_no+this.total_amount+this.waiter_name;
		return s;
	}
}