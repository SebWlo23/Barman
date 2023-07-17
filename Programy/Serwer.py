
from flask import Flask, redirect, render_template, request, jsonify, abort, flash
from flask_sqlalchemy import SQLAlchemy
#from wtforms import TextField, IntegerField, TextAreaField, SubmitField, RadioField, SelectField
from sqlalchemy import desc ,asc
from flask_marshmallow import Marshmallow
from marshmallow import Schema, fields, pprint
from datetime import datetime, timedelta
import  os
from os.path import isfile, join
from os import listdir
import json
from io import StringIO
from werkzeug.wrappers import Response
import itertools
import random
import string

app = Flask(__name__)
SQLALCHEMY_DATABASE_URI = "mysql+mysqlconnector://{username}:{password}@{hostname}/{databasename}".format(
    username="SebWlo23",
    password="alamakota",
    hostname="SebWlo23.mysql.pythonanywhere-services.com",
    databasename="SebWlo23$barman6",
)
app.config["SQLALCHEMY_DATABASE_URI"] = SQLALCHEMY_DATABASE_URI
app.config["SQLALCHEMY_POOL_RECYCLE"] = 299 # connection timeouts
app.config["SQLALCHEMY_TRACK_MODIFICATIONS"] = False # no warning disruptions

db = SQLAlchemy(app)
ma = Marshmallow(app)


class Users(db.Model):

    __tablename__ = "uzytkownicy"
    id = db.Column(db.Integer, primary_key=True)
    imie = db.Column(db.String(4096))

    def __init__(self, imie):
        self.imie = imie

class UsersSchema(ma.Schema):
    class Meta:

        fields = ('id' ,'imie')


user_schema = UsersSchema()
users_schema = UsersSchema(many=True)


@app.route("/uzytkownicy", methods=["GET"])
def get_all_users():
    user_many = Users.query.all()
    result = users_schema.dump(user_many)
    return jsonify(result)


@app.route("/uzytkownik/<id>", methods=["GET"])
def get_all_user(id):
    user = Users.query.get(id)
    result = user_schema.dump(user)
    return jsonify(result)

@app.route("/uzytkownicy/ostatni", methods=["GET"])
def get_last_user():
    user_many = Users.query.all()
    last_user = user_many[-1]
    result = user_schema.dump(last_user)
    return jsonify(result)

@app.route("/uzytkownicy", methods=["POST"])
def add_user():
    name = request.json["imie"]
    new_user = Users(name)
    db.session.add(new_user)
    db.session.commit()
    user = Users.query.get(new_user.id)
    return user_schema.jsonify(user)
##################################################################################3

class Orders(db.Model):

    __tablename__ = "zamowienia"
    id = db.Column(db.Integer, primary_key=True)
    uzytkownicy_id = db.Column(db.Integer)

    def __init__(self, uzytkownicy_id):
        self.uzytkownicy_id = uzytkownicy_id


class OrdersSchema(ma.Schema):
    class Meta:

        fields = ('id' ,'uzytkownicy_id')


order_schema = OrdersSchema()
orders_schema = OrdersSchema(many=True)


@app.route("/zamowienia", methods=["GET"])
def get_all_orders():
    order_many = Orders.query.all()
    result = orders_schema.dump(order_many)
    return jsonify(result)


@app.route("/zamowienie/<id>", methods=["GET"])
def get_all_order(id):
    order = Orders.query.get(id)
    result = order_schema.dump(order)
    return jsonify(result)

@app.route("/zamowienia/ostatnie", methods=["GET"])
def get_last_oder():
    all_orders = Orders.query.all()
    last_order = all_orders[-1]
    result = order_schema.dump(last_order)
    return jsonify(result)


@app.route("/zamowienia", methods=["POST"])
def add_order():
    order = request.json["uzytkownicy_id"]
    new_order = Orders(order)
    db.session.add(new_order)
    db.session.commit()
    order = Orders.query.get(new_order.id)
    return order_schema.jsonify(order)

###############################################################################################################

class Placed_Orders(db.Model):

    __tablename__ = "zlozone_zamowienie"
    id = db.Column(db.Integer, primary_key=True)
    butelka_id = db.Column(db.Integer)
    zamowienie_id = db.Column(db.Integer)
    ilosc = db.Column(db.Float)
    czy_wykonane = db.Column(db.Boolean)

    def __init__(self, butelka_id, zamowienie_id, ilosc, czy_wykonane):
        self.butelka_id = butelka_id
        self.zamowienie_id = zamowienie_id
        self.ilosc = ilosc
        self.czy_wykonane = czy_wykonane


class Placed_OrdersSchema(ma.Schema):
    class Meta:

        fields = ('id' ,'butelka_id' ,'zamowienie_id' ,'ilosc' ,'czy_wykonane')


placed_order_schema = Placed_OrdersSchema()
placed_orders_schema = Placed_OrdersSchema(many=True)


@app.route("/zlozone_zamowienia", methods=["GET"])
def get_all_placed_orders():
    placed_order_many = Placed_Orders.query.all()
    result = placed_orders_schema.dump(placed_order_many)
    return jsonify(result)


@app.route("/zlozone_zamowienie/<id>", methods=["GET"])
def get_all_placed_order(id):
    placed_order = Placed_Orders.query.get(id)
    result = placed_order_schema.dump(placed_order)
    return jsonify(result)


@app.route("/zlozone_zamowienia/ostatnie", methods=["GET"])
def get_last_placed_oder():
    all_orders = Placed_Orders.query.all()
    last_order = all_orders[-1]
    result = placed_order_schema.dump(last_order)
    return jsonify(result)


@app.route("/zlozone_zamowienia", methods=["POST"])
def add_placed_order():
    bottle_id = request.json["butelka_id"]
    order_id = request.json["zamowienie_id"]
    quantity = request.json["ilosc"]
    if_done = request.json["czy_wykonane"]
    new_placed_order = Placed_Orders(bottle_id, order_id, quantity, if_done)
    db.session.add(new_placed_order)
    db.session.commit()
    placed_order = Placed_Orders.query.get(new_placed_order.id)
    return placed_order_schema.jsonify(placed_order)

@app.route('/zlozone_zamowienia/aktualizuj/<id>', methods=['PUT'])
def update_placed_order(id):
    placed_order = Placed_Orders.query.get(id)
    czy_wykonane = request.json['czy_wykonane']
    placed_order.czy_wykonane = czy_wykonane
    db.session.commit()

    return placed_order_schema.jsonify(czy_wykonane)



####################################################################################################

class Bottles(db.Model):

    __tablename__ = "butelka"
    id = db.Column(db.Integer, primary_key=True)
    rodzaj = db.Column(db.String(4096))
    pojemnosc = db.Column(db.Float)
    urzadzenie_id = db.Column(db.Integer)
    czy_nalane = db.Column(db.Boolean)


    def __init__(self, rodzaj, pojemnosc, urzadzenie_id, czy_nalane):
        self.rodzaj = rodzaj
        self.pojemnosc = pojemnosc
        self.urzadzenie_id = urzadzenie_id
        self.czy_nalane = czy_nalane


class BottlesSchema(ma.Schema):
    class Meta:

        fields = ('id' ,'rodzaj' ,'pojemnosc' ,'urzadzenie_id' ,'czy_nalane')


bottle_schema = BottlesSchema()
bottles_schema = BottlesSchema(many=True)


@app.route("/butelki", methods=["GET"])
def get_all_bottles():
    bottle_many = Bottles.query.all()
    result = bottles_schema.dump(bottle_many)
    return jsonify(result)


@app.route("/butelka/<id>", methods=["GET"])
def get_all_bottle(id):
    bottle = Bottles.query.get(id)
    result = bottle_schema.dump(bottle)
    return jsonify(result)


@app.route("/butelki", methods=["POST"])
def add_bottle():
    type = request.json["rodzaj"]
    capacity = request.json["pojemnosc"]
    device_id = request.json["urzadzenie_id"]
    if_poured = request.json["czy_nalane"]
    new_bottle = Bottles(type, capacity, device_id, if_poured)
    db.session.add(new_bottle)
    db.session.commit()
    bottle = Bottles.query.get(new_bottle.id)
    return bottle_schema.jsonify(bottle)


#############################################################################################

class Devices(db.Model):

    __tablename__ = "urzadzenie"
    id = db.Column(db.Integer, primary_key=True)
    adres = db.Column(db.String(4096))
    nazwa = db.Column(db.String(4096))

    def __init__(self, adres, nazwa):
        self.adres = adres
        self.nazwa = nazwa

class DevicesSchema(ma.Schema):
    class Meta:

        fields = ('id' ,'adres' ,'nazwa')


device_schema = DevicesSchema()
devices_schema = DevicesSchema(many=True)


@app.route("/urzadzenia", methods=["GET"])
def get_all_devices():
    device_many = Devices.query.all()
    result = devices_schema.dump(device_many)
    return jsonify(result)


@app.route("/urzadzenie/<id>", methods=["GET"])
def get_all_device(id):
    device = Devices.query.get(id)
    result = device_schema.dump(device)
    return jsonify(result)


@app.route("/urzadzenia", methods=["POST"])
def add_device():
    address = request.json["adres"]
    name = request.json["nazwa"]
    new_device = Devices(address, name)
    db.session.add(new_device)
    db.session.commit()
    device = Devices.query.get(new_device.id)
    return device_schema.jsonify(device)
