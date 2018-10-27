from flask import Flask
from flask_sqlalchemy import SQLAlchemy


app = Flask(__name__, static_folder='resources/public', template_folder='templates')

app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:////resources/database/tmgr.sqlite'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = True

db = SQLAlchemy(app)