require 'buildr/scala'

repositories.remote << "http://www.ibiblio.org/maven2/"
SERVLET='javax.servlet:servlet-api:jar:2.5'

define 'scalatests' do
  project.version = '0.0.1'
  compile.with SERVLET
  package :war
end
