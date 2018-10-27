from datetime import *

from gantt.const import *
from app import db


class Task(db.Model):

    __tablename__ = 'Task'
    id = db.Column(db.Integer, primary_key=True)
    title = db.Column(db.String(255), nullable=False)
    typ = db.Column(db.String(32), nullable=False)
    style = db.Column(db.String(64))

    pid = db.Column(db.Integer, db.ForeignKey('Task.id'))
    sub = db.relationship('Task', lazy='dynamic')    
    
    def __init__(self):
        self.info = []
        self.plan = []
        self.sub = []
            
    def __repr__(self):
        return '<Task %r>' % self.title

