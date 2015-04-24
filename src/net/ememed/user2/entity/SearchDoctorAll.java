package net.ememed.user2.entity;

import java.util.List;

public class SearchDoctorAll {
	private List<SearchPerson> search_person;
	private List<SearchTxtconsult> search_txtconsult;
	private List<SearchHospital> search_hospital;
	private List<SearchGuahao> search_guahao;
	public List<SearchPerson> getSearch_person() {
		return search_person;
	}
	public List<SearchTxtconsult> getSearch_txtconsult() {
		return search_txtconsult;
	}
	public List<SearchHospital> getSearch_hospital() {
		return search_hospital;
	}
	public List<SearchGuahao> getSearch_guahao() {
		return search_guahao;
	}
	public void setSearch_person(List<SearchPerson> search_person) {
		this.search_person = search_person;
	}
	public void setSearch_txtconsult(List<SearchTxtconsult> search_txtconsult) {
		this.search_txtconsult = search_txtconsult;
	}
	public void setSearch_hospital(List<SearchHospital> search_hospital) {
		this.search_hospital = search_hospital;
	}
	public void setSearch_guahao(List<SearchGuahao> search_guahao) {
		this.search_guahao = search_guahao;
	}
	
}
