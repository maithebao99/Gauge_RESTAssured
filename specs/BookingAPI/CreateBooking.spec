# Kiểm thử tạo mới 1 booking

## Scenario: Tạo mới 1 booking thành công

* Đọc nội dung body từ file "data/DataBookingAPI/createBooking.json" và lưu vào key "scenario:bodyCreateBooking"
* Call api tạo mới booking
* Verify response sau khi tạo mới booking thành công với json body được lưu ở key "scenario:bodyCreateBooking"
* Lấy "bookingid" từ response tạo mới booking và lưu vào biến "scenario:bookingId"


## Scenario: Tạo mới booking với nhiều json body khác nhau
* Với từng trường hợp trong bảng, thực hiện tạo booking và kiểm tra response
          | bodyCase                   | expectedStatus | compareResponse |
          |----------------------------|----------------|-----------------|
          | fullValidData              | 200            | true            |
          | firstnameEmptyString       | 200            | true            |
          | firstnameNull              | 500            | false           |
          | lastnameNull               | 500            | false           |
          | lastnameEmptyString        | 200            | true            |
          | totalpriceIs0              | 200            | true            |
          | totalpriceNull             | 500            | false           |
          | depositpaidFalse           | 200            | true            |
          | depositpaidNull            | 500            | false           |
          | bookingdatesNull           | 500            | false           |
          | checkinNull                | 500            | false           |
          | checkoutNull               | 500            | false           |
          | addtionalneedsEmptyString  | 200            | true            |
          | addtionalneedsNull         | 200            | true            |
