from flask_sqlalchemy import orm

from app import db
from information import Information


class Task(db.Model):

    __tablename__ = 'Task'
    id = db.Column(db.Integer, primary_key=True)
    title = db.Column(db.String(255), nullable=False)
    typ = db.Column(db.String(32), nullable=False)
    style = db.Column(db.String(64))

    pid = db.Column(db.Integer, db.ForeignKey('Task.id'))
    sid = db.Column(db.Integer, db.ForeignKey('Information.id'))
    eid = db.Column(db.Integer, db.ForeignKey('Information.id'))

    splan = db.relationship('Information',foreign_keys='Task.id', primaryjoin='Information.tid==Task.id')

    sub = db.relationship('Task', lazy='dynamic')    
    info = db.relationship('Information', foreign_keys='Ingormation.tid', backref='task', lazy='dynamic')
    
    def __init__(self, title='', pid=0, typ='', style=''):
        self.title = title
        self.pid = pid
        self.typ = typ
        self.style = style
        
    @orm.reconstructor
    def init_and_load(self):
        self.plan = self.sub.filter(Information.type=='plan').order_by(Information.finish).all()
        l = len(self.plan)
        self.owner = self.plan[-1].owner if l else ''
        self.finish = self.plan[-1].finish if l else ''
        self.status = self.plan[-1].status if l else ''
    

    def complete(self, idx = []):
        self.identity = 'Task' + '_'.join(map(str, idx))
        
        return self
            
    def __str__(self):
        return '[Task:{0}] id={1} typ={2} style={3}'.format(self.title, self.id, self.typ, self.style)

