function Particle(t,s,h,i,o){
    this.mass=t,
    this.x=s,
    this.y=h,
    this.px=s,
    this.py=h,
    this.vx=i,
    this.vy=o,
    this.ax=0,
    this.ay=0,
    this.collided=!1,
    this.color=[],
    this.color[0]=255,
    this.color[1]=Math.round(256/(1+Math.pow(this.mass/1e5,1))),
    this.color[2]=Math.round(256/(1+Math.pow(this.mass/1e4,1))),
    this.color[3]=(16711680+(this.color[1]<<8)+this.color[2]).toString(16),
    this.radius=Math.log(Math.E+t/1e3)
}
