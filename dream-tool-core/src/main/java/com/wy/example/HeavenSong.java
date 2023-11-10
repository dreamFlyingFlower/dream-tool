package com.wy.example;

import java.util.List;

import com.wy.enums.RegexEnum;

/**
 * 作为一些样例的实体类
 *
 * @author 飞花梦影
 * @date 2021-11-05 14:39:43
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class HeavenSong {
	
	public static void main(String[] args) {
		String sss = "12734256456";
		System.out.println(sss.matches(RegexEnum.REGEX_PHONE.toString()));
	}

	private Long userId;

	private String username;

	private Character sex;

	private Integer age;

	private Double salary;

	private Boolean chinese;

	private List<String> persons;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Character getSex() {
		return sex;
	}

	public void setSex(Character sex) {
		this.sex = sex;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Double getSalary() {
		return salary;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}

	public Boolean getChinese() {
		return chinese;
	}

	public void setChinese(Boolean chinese) {
		this.chinese = chinese;
	}

	public List<String> getPersons() {
		return persons;
	}

	public void setPersons(List<String> persons) {
		this.persons = persons;
	}

	@Override
	public String toString() {
		return "HeavenSong [userId=" + userId + ", username=" + username + ", sex=" + sex + ", age=" + age + ", salary="
				+ salary + ", chinese=" + chinese + ", persons=" + persons + "]";
	}
}