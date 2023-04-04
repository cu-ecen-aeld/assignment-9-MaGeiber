# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "git://github.com/cu-ecen-aeld/assignment-7-MaGeiber;protocol=https;branch=master \
           file://0001-Changing-makefile.patch \
           file://scull-start-stop \
           "

# Modify these as desired
PV = "1.0+git${SRCPV}"
SRCREV = "3bd01889bd406df6e76e487939b002cfe06ee28c"

S = "${WORKDIR}/git"

inherit module

EXTRA_OEMAKE:append:task-install = " -C ${STAGING_KERNEL_DIR} M=${S}/scull"
EXTRA_OEMAKE += "KERNELDIR=${STAGING_KERNEL_DIR}"

inherit update-rc.d
INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME:${PN} = "scull-start-stop"

FILES:${PN} += "${bindir}/scull_load"
FILES:${PN} += "${bindir}/scull_unload"
FILES:${PN} += "${binddir}/*"
FILES:${PN} += "${sysconfdir}/*"


do_configure () {
	:
}

do_compile () {
	oe_runmake
}

do_install () {
	# TODO: Install your binaries/scripts here.
	# Be sure to install the target directory with install -d first
	# Yocto variables ${D} and ${S} are useful here, which you can read about at 
	# https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-D
	# and
	# https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-S
	# See example at https://github.com/cu-ecen-aeld/ecen5013-yocto/blob/ecen5013-hello-world/meta-ecen5013/recipes-ecen5013/ecen5013-hello-world/ecen5013-hello-world_git.bb
	
	#from poky/meta/conf/bitbake.conf
	
	#bindir = usr/bin
	install -d ${D}${bindir}
	install -m 0755 ${S}/scull/scull_load ${D}${bindir}/
	install -m 0755 ${S}/scull/scull_unload ${D}${bindir}/
	
	#sysconfdir = etc, so etc/init.d
	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/scull-start-stop ${D}${sysconfdir}/init.d
	
	#base_libdir = /lib
	install -d ${D}${base_libdir}/modules/${KERNEL_VERSION}
	install -m 0755 ${S}/scull/scull.ko ${D}${base_libdir}/modules/${KERNEL_VERSION}

}
