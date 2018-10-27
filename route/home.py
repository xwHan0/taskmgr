from flask import render_template

from app import *

@app.route('/')
def index():
    return render_template('gantt.html',gantt=[])
