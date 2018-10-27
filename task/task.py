from datetime import *

from gantt.const import *
from global.app import *


class Task(db.Model):

    __tablename__ = 'Task'
    id = db.Column(db.Integer, primary_key=True)
    pid = db.Column(db.Integer, nullable=False)
    title = db.Column(db.String(255), nullable=False)
    typ = db.Column(db.String(32), nullable=False)
    style = db.Column(db.String(64))

    def __init__(self):
        self.info = []
        self.plan = []
        self.sub = []
            
    def __repr__(self):
        return '<Task %r>' % self.title

