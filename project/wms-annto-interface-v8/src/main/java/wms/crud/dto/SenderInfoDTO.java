package wms.crud.dto;

/**
 * Created by PC-4 on 2017/7/11.
 */
public class SenderInfoDTO {

    //"company": "公司，string (200)",
    private String company;

    //"name": "联系人，string (50)",
    private String name;

    //"mobile": "手机号，string (50)",
    private String mobile;

    //"phone": "电话，string (50)",
    private String phone;

    //"province": "省，string (50)",
    private String province;

    //"city": "市，string (50)",
    private String city;

    //"area": "区，string (50)",
    private String area;

    //"address": "详细地址，string (200)",
    private String address;

    //"zipCode": "邮政编码，string (10)"
    private String zipCode;

    public SenderInfoDTO() {
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
