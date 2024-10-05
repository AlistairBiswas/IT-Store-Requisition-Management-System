use ProjectDB

drop table Manager
--drop table UserRequest
drop table ResolvedDelivary
drop table UnresolvedDelivary
drop table ActiveUserRequest
drop table Product
drop table Users

create table Manager
(
AdminID int identity (1, 1) primary key,
AdminName varchar(30) not null,
Password varchar(32) not null
);

insert into Manager (AdminName, Password) values ('admin123', '123456');

create table Users
(
BranchID int identity (1001, 1) primary key,
Branch varchar(30) not null,
Password varchar(32) not null,
Location varchar(30) not null
);

create table Product
(
ModelID int identity (1001, 1) primary key,
ModelName varchar(30) not null,
Category varchar(30) not null,
Quantity int null,
Price decimal(9,2) not null
);

insert into Users (Branch, Password, Location) values 
('Gulshan01', '111111', 'Gulshan'),
('Gulshan02', '222222', 'Gulshan'),
('Dhanmondi', '333333', 'Dhanmondi'),
('Multiplan', '444444', 'Eliphant Road'),
('IDB', '55555', 'Agargoan'),
('Uttara01', '666666', 'Uttara'),
('Uttara02', '777777', 'Uttara');

--create table UserRequest
--(
--OrderID int identity(1,1) primary key not null,
--ProductID int not null foreign key references Product(ModelID),
--Quantity int null,
--Price decimal(9,2) not null,
--Date date not null,
--BranchID int not null foreign key references Users(BranchID)
--);

create table ActiveUserRequest
(
OrderID int primary key identity(1,1),
ProductID int not null foreign key references Product(ModelID),
Quantity int null,
Price decimal(20,2) not null,
Date date not null,
Status varchar(30) default 'pending',
BranchID int not null foreign key  references Users(BranchID)
);

create table ResolvedDelivary
(
OrderID int primary key not null foreign key references ActiveUserRequest(OrderID),
ProductID int not null foreign key references Product(ModelID),
Quantity int null,
Price decimal(9,2),
Date date not null,
BranchID int not null foreign key  references Users(BranchID)
);

create table UnresolvedDelivary
(
OrderID int primary key not null foreign key references ActiveUserRequest(OrderID),
ProductID int not null foreign key references Product(ModelID),
Quantity int null,
Date date not null,
BranchID int not null foreign key  references Users(BranchID)
);

--drop table Product

--truncate table Product

insert into Product (ModelName, Category, Quantity, Price) values
('LG 22MK600M', 'Monitor', 200, 16500.00),
('MSI PRO MP221', 'Monitor', 100, 11500.00),
('AMD Ryzen5 5600G', 'Processor', 300, 17500.00),
('Intel i5-12400', 'Processor', 400, 22000.00),
('Aorus M2', 'Mouse', 200, 1100.00),
('Logitech M90','Mouse',150,400.00),
('Redragon Daksa k576R','Keyboard',100,3000.00),
('Havit KB86l','Keyboard',200,3400.00),
('Seagate Barrcuda ','HDD',150,3500.00),
('Toshiba 5400RPM ','HDD',200,3600.00),
('G.Skill Trident Z 16GB ','RAM',300,5700.00),
('Adata XPG Gammix','RAM',350,6300.00),
  ('Samsung 980','SSD',100,4600.00),
 ( 'Gigabyte 480GB 2.5','SSD',400,5100.00),
( 'GIGABYTE GeForce RTX 2060','Graphics Card',400,34500.00),
('MSI GeForce RTX 3060Ti','Graphics Card',200,57000.00),
('Deepcool AK400','CPU COOLER',100,2500.00),
('Gigabyte AORUS ATC800','CPU COOLER',400,8400.00),
('Antec NX410','Casing',200,5400.00),
('Montech X3','Casing',100,5100.00),

('Thermaltake SMART BM2','Power Supply',400,7000.00),
('Corsair CV650', 'Power Supply',300,5950.00),

('MaxGreen MG','UPS', 100,9100.00),
('Power Guard PG650VA','UPS',400,3100.00),
('Fantech MP292','MousePad',200,200.00),
('Motospeed P70 Pro','MousePad',250,400.00);

--select *from Product