package com.hjw.webService.client.TCI.bean;

import java.util.List;
public class TransmitData1 {
	private List<TJzongJian> tjzongjian;
	private List<TJxiaoJie> tjxiaojie;
	private List<TJkeshijc> tjkeshijc;
	private List<TJjianYan> tjjianyan;
	private List<TJjianCha> tjjiancha;
	
	public List<TJzongJian> getTjzongjian() {
		return tjzongjian;
	}
	public void setTjzongjian(List<TJzongJian> tjzongjian) {
		this.tjzongjian = tjzongjian;
	}
	public List<TJxiaoJie> getTjxiaojie() {
		return tjxiaojie;
	}
	public void setTjxiaojie(List<TJxiaoJie> tjxiaojie) {
		this.tjxiaojie = tjxiaojie;
	}
	public List<TJkeshijc> getTjkeshijc() {
		return tjkeshijc;
	}
	public void setTjkeshijc(List<TJkeshijc> tjkeshijc) {
		this.tjkeshijc = tjkeshijc;
	}
	public List<TJjianYan> getTjjianyan() {
		return tjjianyan;
	}
	public void setTjjianyan(List<TJjianYan> tjjianyan) {
		this.tjjianyan = tjjianyan;
	}
	public List<TJjianCha> getTjjiancha() {
		return tjjiancha;
	}
	public void setTjjiancha(List<TJjianCha> tjjiancha) {
		this.tjjiancha = tjjiancha;
	}
}
