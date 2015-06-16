## Using Android Open Source Project or CyanogenMod

If you are aiming to build this with an AOSP-based ROM (such as AOSP, CyanogenMod, AOKP, PA, Slim, etc) then there are a few steps you need to take to ensure this happens correctly.

### Including in your roomservice.xml/local_manifest.xml

To include this in your AOSP based ROM, you need to add it to your room_service.xml

```XML
<project path="packages/apps/OTAUpdates" name="MatthewBooth/OTAUpdates" revision="aosp" />
```

### Including in your config files

Depending on your source tree, you will need to include the following in one of the config.mk files (usually in the vendor folder or device folder:

```Makefile
PRODUCT_PACKAGES += /
	OTAUpdates
```

If you're building from source, you probably already know how to do this.